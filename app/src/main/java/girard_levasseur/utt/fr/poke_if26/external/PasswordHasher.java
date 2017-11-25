package girard_levasseur.utt.fr.poke_if26.external;

import java.security.MessageDigest;

/**
 * Created by victor on 25/11/17.
 */

public class PasswordHasher {

    /**
     * Hash a password into MD5
     * Thanks to https://stackoverflow.com/a/17490344
     * @param toEncrypt the password to hash
     * @return
     */
    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

}
