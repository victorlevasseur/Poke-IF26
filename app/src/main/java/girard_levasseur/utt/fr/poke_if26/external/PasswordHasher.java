package girard_levasseur.utt.fr.poke_if26.external;

import android.support.v4.content.res.TypedArrayUtils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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
    public static final PasswordHash hash(final String toEncrypt) {



        try {
            //Génération d'un salt sécurisé
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[32];
            random.nextBytes(salt);

            String hash = null;

            try {

                //API26+
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA256");
                KeySpec keySpec = new PBEKeySpec(toEncrypt.toCharArray(), salt, 100, 256);
                SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
                hash = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            } catch (NoSuchAlgorithmException ex) {
                //API < 26
                final MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(salt);
                digest.update(toEncrypt.getBytes());
                final byte[] bytes = digest.digest();
                hash = Base64.getEncoder().encodeToString(bytes);

            }

            return  new PasswordHash(hash,  Base64.getEncoder().encodeToString(salt));
        } catch (Exception exc) {
            return ""; // Impossibru!
        }

    }

}
