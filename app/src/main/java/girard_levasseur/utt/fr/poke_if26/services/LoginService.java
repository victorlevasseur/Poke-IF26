package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Service that manages the logged-in user.
 *
 * Created by victor on 23/11/17.
 */
public interface LoginService {

    /**
     * Log in an user.
     * @param username the username
     * @param password the password
     * @return a single that emits the user if the login succeed.
     */
    Single<User> login(String username, char[] password);

    /**
     * Log out the connected user.
     * @throws ImpossibleActionException if no users are connected
     */
    void logout() throws ImpossibleActionException;

    /**
     * Get the connected user.
     * @return the connected user or null if no users are connected.
     */
    User getConnectedUser();

    /**
     * Refresh the connected user entity by fetching it from the database. Useful after an username
     * change or a password change.
     * @return a completable that completes after the user has been refreshed.
     * @throws ImpossibleActionException if no users are connected
     */
    Completable refreshConnectedUser() throws ImpossibleActionException;

}
