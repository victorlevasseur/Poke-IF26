package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.exceptions.BadCredentialsException;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.types.User;

/**
 * Service that manages the logged-in user.
 *
 * Created by victor on 23/11/17.
 */
public interface LoginService {

    User login(String username, char[] password) throws BadCredentialsException, ImpossibleActionException;

    void logout() throws ImpossibleActionException;

    User getConnectedUser();

}
