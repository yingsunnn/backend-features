package ying.backend_features.jwt;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class RSAKeyPairGenerator {

    public Map<String, String> generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, String> keys = new HashMap<>();

        keys.put(JWTCons.BASE64_PUBLIC_KEY, Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        keys.put(JWTCons.BASE64_PRIVATE_KEY, Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

        return keys;
    }
}
