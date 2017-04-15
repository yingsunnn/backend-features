# backend-features

## Support https

**Configuration:**  
[SpringConfiguration.java](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/SpringConfiguration.java)

```java
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                Ssl ssl = new Ssl();
                ssl.setKeyStore("server.jks");
                ssl.setKeyStorePassword("123456");
                container.setSsl(ssl);
                container.setPort(8443);
            }
        };
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        factory.addAdditionalTomcatConnectors(createHttpConnector());
        return factory;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(8080);
        connector.setRedirectPort(8443);
        return connector;
    }
```

## Secure password
[Password utils](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/utils/AuthenticationUtils.java)
```java
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
```

```java
    public static byte[] getSalt(int saltLen) throws NoSuchAlgorithmException {
        byte[] salt = new byte[saltLen];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
```

## Parameter unit test
[Test class](https://github.com/yingsunnn/backend-features/blob/master/src/test/java/ying/backend_features/utils/AuthenticationUtilsTest.java)

```java
    @RunWith(JUnitParamsRunner.class)
```

```java
    @Test
    @Parameters({"64", "128"})
    public void testGetSalt(int length) {

        try {
            byte[] actualSalt = AuthenticationUtils.getSalt(length);
            String saltStr = Base64.getEncoder().encodeToString(actualSalt);
            byte[] expactedSalt = Base64.getDecoder().decode(saltStr);

            assertEquals(byteArr2String(expactedSalt), byteArr2String(actualSalt));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
```

```java
    @Test
    @Parameters(method = "testGetSecurePasswordParameters")
    public void testGetSecurePassword(String password) {
        try {
            authenticationUtils.getSecurePassword(password, authenticationUtils.getSalt(64));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private Object testGetSecurePasswordParameters() {
        return $(
                $("123456"),
                $("ewrwrwr")
        );
    }
```

## JWT
**Sign**

[Password utils](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/utils/AuthenticationUtils.java)

```java
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
```
**Verify**
```java
    public void jwtVerify (String token, String secretKey) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);

        System.out.println(jwt.getClaim("role").asString());
    }

```
## Custom parameter annotation

**Custom annotation**

[UserAuthentication](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/parameter_annotation/UserAuthentication.java)

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthentication {

    public static final String MANDATORY = "MANDATORY";
    public static final String OPTIONAL = "OPTIONAL";

    public String value() default MANDATORY;
}
```

**HandlerMethodArgumentResolver**

[UserAuthenticationResolver](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/parameter_annotation/UserAuthenticationResolver.java)

```java

@Component
public class UserAuthenticationResolver implements HandlerMethodArgumentResolver {
    .
    .
    .
}
```

**Configuration**

[AnnotationResolverConfig](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/AnnotationResolverConfig.java)

```java
@Configuration
public class AnnotationResolverConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userAuthenticationResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    public UserAuthenticationResolver userAuthenticationResolver() {
        return new UserAuthenticationResolver();
    }
}
```


