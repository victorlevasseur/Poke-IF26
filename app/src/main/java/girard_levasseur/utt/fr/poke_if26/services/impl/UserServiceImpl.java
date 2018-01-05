package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Base64;
import android.util.Log;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.AlreadyExistingUsernameException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHash;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 26/11/17.
 */
@Singleton
public class UserServiceImpl implements UserService {

    private PokeIF26Database db;

    @Inject
    public UserServiceImpl(PokeIF26Database db) {
        this.db = db;
    }

    @Override
    public Single<User> registerUser(String username, char[] password) {
        return Single.fromCallable(() -> {
            User user = new User();
            user.setUsername(username);

            PasswordHash hash = hashPassword(password);
            user.setPasswordHash(hash.hash);
            user.setSalt(hash.salt);
            erasePassword(password);
            try {
                return db.userDao().insertUser(user);
            } catch(SQLiteConstraintException e) {
                throw new AlreadyExistingUsernameException(e.getMessage());
            }
        }).flatMap(createdId -> db.userDao().getUserById(createdId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable changeUserLogin(User user, String newLogin) {
        return Completable.fromCallable(() -> {
            User userWithNewLogin = user.clone();
            userWithNewLogin.setUsername(newLogin);
            try {
                db.userDao().updateUser(userWithNewLogin);
                return true;
            } catch(SQLiteConstraintException e) {
                throw new AlreadyExistingUsernameException(e.getMessage());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable changeUserPassword(User user, char[] newPassword) {
        return Completable.fromCallable(() -> {
            PasswordHash newPasswordHashed = hashPassword(newPassword);
            User userWithChangedPassword = user.clone();
            userWithChangedPassword.passwordHash = newPasswordHashed.hash;
            userWithChangedPassword.salt = newPasswordHashed.salt;
            db.userDao().updateUser(userWithChangedPassword);
            erasePassword(newPassword);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deleteUser(User user) {
        return Completable.fromCallable(() -> {
            db.pokemonInstanceDao().releasePokemonsOfUser(user.getId());
            db.userDao().deleteUser(user);
            return true;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private PasswordHash hashPassword(char[] password) {
        byte[] salt = PasswordHasher.randomSalt();
        return PasswordHasher.hash(new String(password), salt);
    }

    private void erasePassword(char[] password) {
        Arrays.fill(password, '*');
    }
}
