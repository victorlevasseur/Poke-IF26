package girard_levasseur.utt.fr.poke_if26.services.impl;

import java.util.Arrays;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.services.DatabaseService;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.types.User;

/**
 * Impl of the login service.
 * Created by victor on 23/11/17.
 */
public class LoginServiceImpl implements LoginService {

    private DatabaseService databaseService = null;

    private User loggedUser = null;

    @Inject
    public LoginServiceImpl(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public User login(String username, char[] password)
            throws BadCredentialsException, ImpossibleActionException {
        if (loggedUser != null) {
            this.erasePassword(password);
            throw new ImpossibleActionException(
                    "Impossible à se loguer si un utilisateur l'est déjà !");
        }

        if (username.equals("test") && Arrays.equals(password, new char[]{ 'a', 'b', 'c' })) {
            this.loggedUser = new User("test");
            this.erasePassword(password);
            return this.loggedUser;
        } else {
            this.erasePassword(password);
            throw new BadCredentialsException("Login ou mot de passe incorrect.");
        }
    }

    @Override
    public void logout() throws ImpossibleActionException {
        if (loggedUser == null) {
            throw new ImpossibleActionException("Impossible de se déconnecter si aucun utilisateur est connecté !");
        }
        this.loggedUser = null;
    }

    @Override
    public User getConnectedUser() {
        return this.loggedUser;
    }

    private void erasePassword(char[] password) {
        Arrays.fill(password, '*');
    }
}
