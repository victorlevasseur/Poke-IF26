package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import io.reactivex.Single;

/**
 * Created by victor on 26/11/17.
 */

public interface UserService {

    Single<User> registerUser(String username, char[] password);

}
