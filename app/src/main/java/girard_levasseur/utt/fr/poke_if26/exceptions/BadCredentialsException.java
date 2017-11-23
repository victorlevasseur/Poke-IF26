package girard_levasseur.utt.fr.poke_if26.exceptions;

/**
 * Exception thrown when the login process fails because of bad credentials.
 */
public class BadCredentialsException extends Exception {

    public BadCredentialsException(String message) {
        super(message);
    }

}
