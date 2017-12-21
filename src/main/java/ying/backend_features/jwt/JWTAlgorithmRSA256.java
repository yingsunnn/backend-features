package ying.backend_features.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class JWTAlgorithmRSA256 {

    public Algorithm getAlgorithm(String base64PublicKey, String base64PrivateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        RSAPublicKey rsaPublicKey = null;
        RSAPrivateKey rsaPrivateKey = null;

        if (StringUtils.isNotBlank(base64PublicKey))
            rsaPublicKey = getRSAPublicKey(base64PublicKey);
        if (StringUtils.isNotBlank(base64PrivateKey))
            rsaPrivateKey = getPrivateKey(base64PrivateKey);

        return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    }

    public static RSAPrivateKey getPrivateKey(String Base64PrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decodeBase64(Base64PrivateKey.getBytes()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    public static RSAPublicKey getRSAPublicKey(String base64PublicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey.getBytes()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
