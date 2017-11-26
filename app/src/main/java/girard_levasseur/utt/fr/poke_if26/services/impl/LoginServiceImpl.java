package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.arch.persistence.room.EmptyResultSetException;

import java.util.Arrays;
import java.util.Observable;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Impl of the login service.
 * Created by victor on 23/11/17.
 */
public class LoginServiceImpl implements LoginService {

    private PokeIF26Database db;

    private User loggedUser = null;

    @Inject
    public LoginServiceImpl(PokeIF26Database db) {
        this.db = db;
    }

    @Override
    public Single<User> login(String username, char[] password) {
        if (loggedUser != null) {
            this.erasePassword(password);
            return Single.error(new ImpossibleActionException(
                    "Impossible à se loguer si un utilisateur l'est déjà !"));
        }

        return this.db.userDao().getUserByUsername(username)
                .subscribeOn(Schedulers.io()) // Run the query on the IO scheduler/thread.
                .onErrorResumeNext(err -> // If the user is not found, throw a BadCredentialsException.
                        Single.error(new BadCredentialsException("Login ou mot de passe incorrect.")))
                .map(userWithLogin -> {
                    if (PasswordHasher.md5(new String(password))
                            .equals(userWithLogin.getPasswordHash())) {
                        this.loggedUser = userWithLogin;
                        return this.loggedUser;
                    } else {
                        // The password is invalid, throw a BadCredentialsException.
                        throw new BadCredentialsException("Login ou mot de passe incorrect.");
                    }
                })
                .doFinally(() -> this.erasePassword(password));
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
