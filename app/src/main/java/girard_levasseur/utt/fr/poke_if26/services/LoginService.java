package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.entities.User;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Service that manages the logged-in user.
 *
 * Created by victor on 23/11/17.
 */
public interface LoginService {

    Single<User> login(String username, char[] password);

    void logout() throws ImpossibleActionException;

    User getConnectedUser();

}
