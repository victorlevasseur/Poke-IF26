package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by victor on 26/11/17.
 */

public interface UserService {

    /**
     * Register a new user given its username and password
     * @param username the username
     * @param password the password
     * @return a single emitting the newly registered user.
     *
     * May throw a AlreadyExistingUsernameException error through the single if the username
     * is not available.
     */
    Single<User> registerUser(String username, char[] password);

    /**
     * Change the password of an user.
     * @param user the user object
     * @param newPassword the new password
     * @return a completable that notifies the subscriber when the action is done.
     */
    Completable changeUserPassword(User user, char[] newPassword);

}
