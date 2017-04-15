package ying.backend_features.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.security.MessageDigest.getInstance;

/**
 * Created by ying on 2017-04-14.
 */
public class AuthenticationUtils {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationUtils.class);

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

    public String jwtSign(String secretKey) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withExpiresAt(Date.from(LocalDateTime.now().plus(1, ChronoUnit.WEEKS).atZone(ZoneId.systemDefault()).toInstant()))
                .withNotBefore(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer("Ying")
                .withClaim("role", "admin")
                .sign(algorithm);
        logger.debug("secret token: " + token);

        return token;
    }

    public void jwtVerify (String token, String secretKey) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);

        System.out.println(jwt.getClaim("role").asString());
    }
}
