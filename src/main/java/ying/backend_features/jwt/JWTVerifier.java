package ying.backend_features.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
@AllArgsConstructor
public class JWTVerifier {

    private JWTAlgorithmRSA256 jwtAlgorithmRSA256;

    public JWToken verifyToken(String token, JWToken jwToken, Algorithm algorithm) {
        com.auth0.jwt.JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwToken.getIssuer())
                .withSubject(jwToken.getSubject())
//                .withAudience(jwToken.getAudience())
                .build();
        DecodedJWT jwt = verifier.verify(token);

        return JWToken.builder().build();
    }

    public JWToken verifyToken (String token,
                                JWToken jwToken,
                                String base64PublicKey,
                                String base64PrivateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Algorithm algorithm = jwtAlgorithmRSA256.getAlgorithm(base64PublicKey, base64PrivateKey);

        return verifyToken(token, jwToken, algorithm);
    }
}
