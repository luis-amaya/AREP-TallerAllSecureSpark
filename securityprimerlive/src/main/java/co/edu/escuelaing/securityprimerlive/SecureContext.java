package co.edu.escuelaing.securityprimerlive;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class SecureContext {
    private String user;
    private String password;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassw(String passwd) {
        password = startHashOperation(passwd);
    }

    private String startHashOperation(String stringToBHashed) {
        String sha256hex = Hashing.sha256().hashString(stringToBHashed, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    public static SecureContext initSecureContext() {
        return new SecureContext();
    }

    public Boolean validate(String user, String passwd) {
        String passwdToBCompared = startHashOperation(passwd);
        if (user.equals(this.user) && password.equals(passwdToBCompared)) {
            return true;
        }
        return false;
    }

}
