package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.util.Log;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHash;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.util.Base64;

/**
 * Impl of the login service.
 * Created by victor on 23/11/17.
 */
@Singleton
public class LoginServiceImpl implements LoginService {

    private PokeIF26Database db;

    private User loggedUser = null;

    @Inject
    public LoginServiceImpl(PokeIF26Database db) {
        this.db = db;
    }

    @Override
    public Single<User> login(String username, char[] password) {
        synchronized (this) {
            if (loggedUser != null) {
                this.erasePassword(password);
                return Single.error(new ImpossibleActionException(
                        "Impossible à se loguer si un utilisateur l'est déjà !"));
            }
        }

        return this.db.userDao().getUserByUsername(username)
                .subscribeOn(Schedulers.io()) // Run the query on the IO scheduler/thread.
                .onErrorResumeNext(err -> // If the user is not found, throw a BadCredentialsException.
                        Single.error(new BadCredentialsException("Login ou mot de passe incorrect."))
                )
                .map(userWithLogin -> {
                    PasswordHash hash = PasswordHasher.hash(new String(password), Base64.decode(userWithLogin.getSalt(), 0));

                    if (hash.hash.equals(userWithLogin.getPasswordHash())) {
                        synchronized (this) {
                            // Lock the whole service before changing the user reference.
                            this.loggedUser = userWithLogin;
                        }

                        return this.loggedUser;
                    } else {
                        // The password is invalid, throw a BadCredentialsException.
                        throw new BadCredentialsException("Login ou mot de passe incorrect.");
                    }
                })
                .doFinally(() -> this.erasePassword(password));
    }

    @Override
    public synchronized void logout() throws ImpossibleActionException {
        if (loggedUser == null) {
            throw new ImpossibleActionException("Impossible de se déconnecter si aucun utilisateur est connecté !");
        }
        this.loggedUser = null;
    }

    @Override
    public synchronized User getConnectedUser() {
        return this.loggedUser;
    }

    @Override
    public Completable refreshConnectedUser() throws ImpossibleActionException {
        if (loggedUser == null) {
            throw new ImpossibleActionException("Impossible de rafraichir l'utilisateur si aucun utilisateur est connecté !");
        }
        return db.userDao().getUserById(loggedUser.getId())
                .doOnSuccess((refreshedUser) -> {
                    synchronized (this) {
                        // Lock the whole service before changing the user reference.
                        loggedUser = refreshedUser;
                    }
                })
                .toCompletable()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private void erasePassword(char[] password) {
        Arrays.fill(password, '*');
    }
}
