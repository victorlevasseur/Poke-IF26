package girard_levasseur.utt.fr.poke_if26.external;

import android.support.v4.content.res.TypedArrayUtils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import android.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by victor on 25/11/17.
 */

public class PasswordHasher {

    public static final byte[] randomSalt() {
        //Génération d'un salt sécurisé
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hash a password with PBKDF2 or SHA256
     * @param toEncrypt the password to hash
     * @param salt a random salt
     * @return
     */
    public static final PasswordHash hash(final String toEncrypt, byte[] salt) {
        try {

            String hash = null;

            try {
                //API26+
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA256");
                KeySpec keySpec = new PBEKeySpec(toEncrypt.toCharArray(), salt, 100, 256);
                SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
                hash = Base64.encodeToString(secretKey.getEncoded(), 0, secretKey.getEncoded().length, 0);

            } catch (NoSuchAlgorithmException ex) {
                //API < 26
                final MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(salt);
                digest.update(toEncrypt.getBytes());
                final byte[] bytes = digest.digest();
                hash = Base64.encodeToString(bytes, 0, bytes.length, 0);

            }

            return  new PasswordHash(hash,  Base64.encodeToString(salt, 0, salt.length, 0));
        } catch (Exception exc) {
            return new PasswordHash("", ""); // Impossibru!
        }

    }

}
