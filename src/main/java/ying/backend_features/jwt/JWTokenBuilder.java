package ying.backend_features.jwt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@AllArgsConstructor
public class JWTokenBuilder {

    public static JWToken build (UserProfile userProfile) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);

        return JWToken.builder()
                .issuer(JWToken.POP_DICTIONARY)
                .subject(String.valueOf(userProfile.getId()))
                .issuedAt(new Date())
                .expirationTime(calendar.getTime())
                .build();
    }

    public static JWToken buildVerificationJWToken (UserProfile userProfile) {
        return JWToken.builder()
                .issuer(JWToken.POP_DICTIONARY)
                .subject(String.valueOf(userProfile.getId()))
                .build();
    }
}
