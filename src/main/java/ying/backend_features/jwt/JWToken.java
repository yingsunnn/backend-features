package ying.backend_features.jwt;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class JWToken {

    public static final String POP_DICTIONARY = "POP Dictionary";

    private String algorithm;
    private String type;
    private String content;
    private String kid;

    private String issuer;
    private String subject; // user id
    private String audience;
    private Date expirationTime;
    private Date notBefore;
    private Date issuedAt;
    private String jwtId;

}
