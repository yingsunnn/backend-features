package ying.backend_features.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.security.MessageDigest.getInstance;

/**
 * Created by ying on 2017-04-14.
 */
public class AuthenticationUtils {
    public static String getSecurePassword(String password, byte[] salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        MessageDigest messageDigest = getInstance("SHA-512");
        messageDigest.update(salt);
        byte[] bytes = messageDigest.digest(password.getBytes("UTF-8"));
        StringBuilder generatedPassword = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            generatedPassword.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return generatedPassword.toString();
    }

    public static byte[] getSalt(int saltLen) throws NoSuchAlgorithmException {
        byte[] salt = new byte[saltLen];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
