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

## Parameter unit test
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

