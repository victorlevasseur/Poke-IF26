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
     * Change an user login.
     * @param user the user
     * @param newLogin the new login
     * @return a completable that notifies the subscriber when the action is done.
     * If an user with the same login exists, the completable errors with a AlreadyExistingUsernameException.
     *
     * Warning: the user is updated in the database, don't forget to fetch again all is instances
     * (see LoginService#refreshConnectedUser).
     */
    Completable changeUserLogin(User user, String newLogin);

    /**
     * Change the password of an user.
     * @param user the user object
     * @param newPassword the new password
     * @return a completable that notifies the subscriber when the action is done.
     */
    Completable changeUserPassword(User user, char[] newPassword);

    /**
     * Delete an user.
     * Also release all the pokemons the user had captured.
     * @param user the user to delete
     * @return a completable that notifies the subscriber when the action is completed.
     */
    Completable deleteUser(User user);

}
