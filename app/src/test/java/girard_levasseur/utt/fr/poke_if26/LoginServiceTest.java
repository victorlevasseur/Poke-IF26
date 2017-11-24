package girard_levasseur.utt.fr.poke_if26;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.impl.LoginServiceImpl;
import girard_levasseur.utt.fr.poke_if26.types.User;

import static org.junit.Assert.*;

/**
 * Created by victor on 24/11/17.
 */
public class LoginServiceTest {

    private LoginService loginService;

    @Before
    public void initializeLoginService() {
        loginService = new LoginServiceImpl();
    }

    @Test
    public void loginValidUser() {
        try {
            assertThat(loginService.login("test", new char[]{'a', 'b', 'c'}),
                    new BaseMatcher<User>() {
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
                    });
        } catch (BadCredentialsException | ImpossibleActionException e) {
            e.printStackTrace();
            fail("loginService.login with test and abc should log in successfully!");
        }
    }

    @Test
    public void loginAlreadyConnectedTest() {
        try {
            loginService.login("test", new char[]{'a', 'b', 'c'});
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            fail("Should log in with test and abc password");
        } catch (ImpossibleActionException e) {
            e.printStackTrace();
            fail("No user should be logged in initially");
        }

        try {
            loginService.login("test", new char[]{'a', 'b', 'c'});
            fail("The second login attempt should fail as an user is already connected!");
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            fail("Should log in with test and abc password");
        } catch (ImpossibleActionException ignored) {

        }
    }

    @Test
    public void loginBadCredentialTest() {
        try {
            loginService.login("toto", new char[]{'a', 'b', 'c'});
            fail("The login should fail with bad credentials!");
        } catch (BadCredentialsException ignored) {

        } catch (ImpossibleActionException e) {
            fail("No user should be logged in initially");
        }

        try {
            loginService.login("test", new char[]{'a', 'b', 'c', 'd'});
            fail("The login should fail with bad credentials!");
        } catch (BadCredentialsException ignored) {

        } catch (ImpossibleActionException e) {
            fail("No user should be logged in initially");
        }
    }

}
