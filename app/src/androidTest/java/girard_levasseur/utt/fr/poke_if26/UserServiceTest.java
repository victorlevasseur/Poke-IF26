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
import girard_levasseur.utt.fr.poke_if26.exceptions.AlreadyExistingUsernameException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import girard_levasseur.utt.fr.poke_if26.services.impl.UserServiceImpl;
import girard_levasseur.utt.fr.poke_if26.tools.ImmediateSchedulersRule;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by victor on 24/11/17.
 */
@RunWith(AndroidJUnit4.class)
public class UserServiceTest {

    @Rule
    public final ImmediateSchedulersRule immediateSchedulersRule = new ImmediateSchedulersRule();

    private PokeIF26Database db;

    private UserService userService;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, PokeIF26Database.class)
                .allowMainThreadQueries()
                .build();

        User user = new User();
        user.setUsername("alreadyHereUsername");
        user.setPasswordHash(PasswordHasher.md5("abc"));

        db.userDao().insertUser(user);

        // Init the login service.
        userService = new UserServiceImpl(db);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


    @Test
    public void registerUser() {
        TestObserver<User> observer = new TestObserver<>();

        userService.registerUser("toto", new char[]{'a', 'z', 'e', 'r', 't', 'y'})
                .subscribe(observer);

        // Assert the returned User object.
        observer.assertComplete();
        List<User> returnedUser = observer.values();
        assertEquals(returnedUser.size(), 1);
        BaseMatcher<User> testUserMatcher = new BaseMatcher<User>() {
            @Override
            public boolean matches(Object item) {
                if (item instanceof User) {
                    User user = (User)item;
                    return "toto".equals(user.username);
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {

            }
        };
        assertThat(returnedUser.get(0), testUserMatcher);

        // Assert the db
        assertThat(db.userDao().getUserByIdSync(returnedUser.get(0).getId()), testUserMatcher);
    }

    @Test
    public void registerConflict() {
        TestObserver<User> observer = new TestObserver<>();

        userService.registerUser("alreadyHereUsername", new char[]{'a', 'z', 'e', 'r', 't', 'y'})
                .subscribe(observer);

        // Assert the returned User object.
        observer.assertError(AlreadyExistingUsernameException.class);
    }

}
