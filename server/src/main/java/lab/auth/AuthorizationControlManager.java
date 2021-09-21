package lab.auth;

import lab.DatabaseManager;

import java.math.BigInteger;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationControlManager {

    private final DatabaseManager databaseManager;

    public AuthorizationControlManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean checkCredentials(Credentials credentials) {
        String hashedPassword;
        try {
            hashedPassword = hashPassword(credentials.password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to find MD2 algorithm: " + e.getMessage());
            return false;
        }

        try {
            return databaseManager.checkCredentials(credentials.username, hashedPassword);
        } catch (SQLException e) {
            System.err.println("Failed to check credentials: " + e.getMessage());

            return false;
        }
    }

    public boolean createNewUser(Credentials credentials) {
        String hashedPassword;
        try {
            hashedPassword = hashPassword(credentials.password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to find MD2 algorithm: " + e.getMessage());
            return false;
        }

        try {
            databaseManager.createNewUser(credentials.username, hashedPassword);
        } catch (SQLException e) {
            System.err.println("Failed to create user: " + e.getMessage());
            return false;
        }

        return true;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        // getInstance() method is called with algorithm MD2
        MessageDigest md = MessageDigest.getInstance("MD2");

        // digest() method is called
        // to calculate message digest of the input string
        // returned as array of byte
        byte[] messageDigest = md.digest(password.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        StringBuilder hashText = new StringBuilder(no.toString(16));

        // Add preceding 0s to make it 32 bit
        while (hashText.length() < 32) {
            hashText.insert(0, "0");
        }

        // return the HashText
        return hashText.toString();
    }
}
