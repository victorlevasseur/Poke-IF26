package girard_levasseur.utt.fr.poke_if26.exceptions;

/**
 * Exception thrown when there's a conflict with an existing username.
 *
 * Created by victor on 26/11/17.
 */
public class AlreadyExistingUsername extends Exception {

    public AlreadyExistingUsername(String msg) {
        super(msg);
    }

}
