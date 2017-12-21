package ying.backend_features.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
@AllArgsConstructor
public class JWTSigner {

    private JWTAlgorithmRSA256 jwtAlgorithmRSA256;

    public String signToken (JWToken jwToken, Algorithm algorithm) {
        return JWT.create()
                .withIssuer(jwToken.getIssuer())
                .withSubject(jwToken.getSubject())
                .withAudience(jwToken.getAudience())
                .withExpiresAt(jwToken.getExpirationTime())
                .withIssuedAt(jwToken.getIssuedAt())
                .withNotBefore(jwToken.getNotBefore())
                .sign(algorithm);
    }

    public String signToken (JWToken jwToken,
                             String base64PublicKey,
                             String base64PrivateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        Algorithm algorithm = jwtAlgorithmRSA256.getAlgorithm(base64PublicKey, base64PrivateKey);
        return signToken(jwToken, algorithm);
    }
}
