package girard_levasseur.utt.fr.poke_if26.external;

/**
 * Created by Antoine on 30/11/2017.
 */

public class PasswordHash {
    public String hash;
    public String salt;

    public PasswordHash(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }
}
