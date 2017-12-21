package ying.backend_features.jwt;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class JWTClient {

    private RSAKeyPairGenerator rsaKeyPairGenerator;
    private JWTSigner jwtSigner;
    private JWTVerifier jwtVerifier;

    public Map<String, String> getKeypair () throws NoSuchAlgorithmException {
        return rsaKeyPairGenerator.generateKeys();
    }

    public void signToken(Map<String, String> keyPair) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String jwToken = jwtSigner.signToken(
                JWTokenBuilder.build(UserProfile.builder().build()),
                keyPair.get(JWTCons.BASE64_PUBLIC_KEY),
                keyPair.get(JWTCons.BASE64_PRIVATE_KEY));

        System.out.println(jwToken);
    }

    public void verifyToken(String token, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        jwtVerifier.verifyToken(
                token,
                JWTokenBuilder.buildVerificationJWToken(UserProfile.builder().build()),
                publicKey,
                null);
    }

}
