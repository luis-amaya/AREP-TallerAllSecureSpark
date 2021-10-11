package co.edu.escuelaing.securityprimerlive;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.Hashing;

/**
 * Clase utilizada para crear un contexto seguro. Crea hashes para los passwords
 * utilizando SHA-256 Tambien tiene la capacidad de comparar los usuarios y
 * passwords ingresados
 */
public class SecureContext {
    private static Map<String, String> users = new HashMap<String, String>();

    /**
     *
     * @param user
     */
    public void setUser(String user, String passwd) {
        users.put(user, startHashOperation(passwd));
    }

    /**
     *
     * @param stringToBHashed
     * @return
     */
    private String startHashOperation(String stringToBHashed) {
        String sha256hex = Hashing.sha256().hashString(stringToBHashed, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    /**
     *
     * @return
     */
    public static SecureContext initSecureContext() {
        return new SecureContext();
    }

    /**
     *
     * @param user
     * @param passwd
     * @return
     */
    public Boolean validate(String user, String passwd) {
        if (users.containsKey(user) && users.get(user).equals(startHashOperation(passwd))) {
            return true;
        }
        return false;
    }

}
