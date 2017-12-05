package girard_levasseur.utt.fr.poke_if26;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHash;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.impl.LoginServiceImpl;
import girard_levasseur.utt.fr.poke_if26.tools.ImmediateSchedulersRule;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by victor on 24/11/17.
 */
@RunWith(AndroidJUnit4.class)
public class LoginServiceTest {

    @Rule
    public final ImmediateSchedulersRule immediateSchedulersRule = new ImmediateSchedulersRule();

    private PokeIF26Database db;

    private LoginService loginService;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, PokeIF26Database.class)
                .allowMainThreadQueries()
                .build();

        User user = new User();
        user.setUsername("test");
        PasswordHash hash = PasswordHasher.hash("abc", "def".getBytes());
        user.setPasswordHash(hash.hash);
        user.setSalt("def");

        db.userDao().insertUser(user);

        // Init the login service.
        loginService = new LoginServiceImpl(db);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


    @Test
    public void loginValidUser() {
        TestObserver<User> observer = new TestObserver<>();

        loginService.login("test", new char[]{'a', 'b', 'c'})
                .subscribe(observer);

        observer.assertComplete();
        List<User> returnedUser = observer.values();
        assertEquals(returnedUser.size(), 1);

        BaseMatcher<User> testUserMatcher = new BaseMatcher<User>() {
            @Override
            public boolean matches(Object item) {
                if (item instanceof User) {
                    User user = (User)item;
                    return "test".equals(user.username);
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {

            }
        };
        assertThat(returnedUser.get(0), testUserMatcher);
        assertThat(loginService.getConnectedUser(), testUserMatcher);
    }

    @Test
    public void loginAlreadyConnectedTest() {
        {
            TestObserver<User> observer = new TestObserver<>();
            loginService.login("test", new char[]{'a', 'b', 'c'})
                    .subscribe(observer);
            observer.assertComplete();
        }

        {
            TestObserver<User> observer = new TestObserver<>();
            loginService.login("test", new char[]{'a', 'b', 'c'})
                    .subscribe(observer);
            observer.assertError(ImpossibleActionException.class);
        }
    }

    @Test
    public void loginBadCredentialTest() {
        {
            TestObserver<User> observer = new TestObserver<>();
            loginService.login("toto", new char[]{'a', 'b', 'c'})
                    .subscribe(observer);
            observer.assertError(BadCredentialsException.class);
        }

        {
            TestObserver<User> observer = new TestObserver<>();
            loginService.login("test", new char[]{'a', 'b', 'c', 'd'})
                    .subscribe(observer);
            observer.assertError(BadCredentialsException.class);
        }
    }

}
