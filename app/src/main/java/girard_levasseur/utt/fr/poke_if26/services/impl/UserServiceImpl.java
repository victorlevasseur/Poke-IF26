package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.database.sqlite.SQLiteConstraintException;

import java.util.Arrays;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.AlreadyExistingUsernameException;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHash;
import girard_levasseur.utt.fr.poke_if26.external.PasswordHasher;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 26/11/17.
 */

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
            PasswordHash hash = PasswordHasher.hash(new String(password), PasswordHasher.randomSalt());
            user.setPasswordHash(hash.hash);
            user.setSalt(hash.salt);
            erasePassword(password);
            try {
                return db.userDao().insertUser(user);
            } catch(SQLiteConstraintException e) {
                throw new AlreadyExistingUsernameException(e.getMessage());
            }
        }).flatMap(createdId -> db.userDao().getUserById(createdId)).subscribeOn(Schedulers.io());
    }


    private void erasePassword(char[] password) {
        Arrays.fill(password, '*');
    }
}
