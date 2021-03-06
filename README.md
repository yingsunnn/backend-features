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

[AnnotationResolverConfig](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/parameter_annotation/AnnotationResolverConfig.java)

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

## Custom method annotation
[Tutorial](http://blog.javaforge.net/post/76125490725/spring-aop-method-interceptor-annotation)

**Implement MethodInterceptor**

[ServicePermissionsNeedMethodInterceptor](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/method_annotation/ServicePermissionsNeedMethodInterceptor.java)

```java
public class ServicePermissionsNeedMethodInterceptor implements MethodInterceptor {
    .
    .
    .
}
```

**Implement AbstractPointcutAdvisor**

[ServicePermissionsNeedAdvisor](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/method_annotation/ServicePermissionsNeedAdvisor.java)

```java
@Component
public class ServicePermissionsNeedAdvisor extends AbstractPointcutAdvisor {
    private final StaticMethodMatcherPointcut pointcut = new
            StaticMethodMatcherPointcut() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    return method.isAnnotationPresent(ServicePermissionsNeed.class);
                }
            };

    @Autowired
    private ServicePermissionsNeedMethodInterceptor interceptor;


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return interceptor;
    }
}
```

**Costom annotation**

[ServicePermissionsNeed](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/method_annotation/ServicePermissionsNeed.java)

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServicePermissionsNeed {
    public String[] value() default {"read_post", "read_reply"};
}
```

**Configuration**

[MethodInterceptorConfig](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/method_annotation/MethodInterceptorConfig.java)

```java
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);

        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public ServicePermissionsNeedMethodInterceptor getPermissionMethodInterceptor() {
        return new ServicePermissionsNeedMethodInterceptor();
    }
```
## Redis options

**Redis pool config**

[RedisPoolConfig](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/redis_options/RedisPoolConfig.java)

```java
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setJmxEnabled(true);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHostName);
        jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setPort(redisPort);
        return jedisConnectionFactory;
    }

    @Bean
    @Autowired
    public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
```

**Options**

[RedisOptionsController](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/redis_options/RedisOptionsController.java)

## Websocket 1

**Config**

[WebSocketConfig1](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_1/WebSocketConfig1.java)

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig1 extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        messageBrokerRegistry.enableSimpleBroker("/ws_send_to");
//        messageBrokerRegistry.setApplicationDestinationPrefixes("/wschat");
//        messageBrokerRegistry.setUserDestinationPrefix("/userTest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws1/chat/{room_id}").setAllowedOrigins("*").withSockJS();

    }
}
```

**Controller**

[WebSocketController1](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_1/WebSocketController1.java)

```java
    /**
     * Websocket example
     */
    @MessageMapping("/ws1/chat/{room_id}")
    @SendTo("/ws_send_to/chat/{room_id}") 
    public DirectMessage reply(DirectMessage directMessage, @PathVariable("room_id") String roomId) throws Exception {
        logger.debug("room id: " + roomId + " directMessage: " + directMessage);
        return directMessage;
    }
```

**Page**

[websocket1.html](https://github.com/yingsunnn/backend-features/blob/master/src/main/resources/static/websocket1.html)

```javascript
    var socket = new SockJS('/ws1/chat/' + roomId);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/ws_send_to/chat/' + roomId , function(message) {
            .
            .
            .
        });
    });

    function sendReply() {
        var message = document.getElementById('message').value;
        var roomId = document.getElementById('room_id').value;
        stompClient.send('/ws1/chat/' + roomId, {}, JSON.stringify({
            'message' : message
        }));
    }
```

## Websocket 2

**Implement HandshakeInterceptor**

[HandShake](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_2/HandShake.java)

```java
public class HandShake implements HandshakeInterceptor {
    .
    .
    .
}
```

**Implement WebSocketHandler**

[MyWebSocketHandler](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_2/MyWebSocketHandler.java)

```java

@Component
public class MyWebSocketHandler implements WebSocketHandler {
    .
    .
    .
}
```

**Configuration**

[WebSocketHandlerConfigurer](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_2/WebSocketHandlerConfigurer.java)

```java
@Component
@EnableWebSocket
public class WebSocketHandlerConfigurer implements WebSocketConfigurer {

    @Autowired
    MyWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//        webSocketHandlerRegistry.addHandler(handler, "/ws2/test").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
        webSocketHandlerRegistry.addHandler(handler, "/ws2/post/{board_id}/sockjs").addInterceptors(new HandShake()).setAllowedOrigins("*").withSockJS();
    }
}

```

**Boardcast controller**

[WebSocketController2](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/websocket_2/WebSocketController2.java)

```java
    @RequestMapping(value = "ws2/boardcast/{title}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String websocketBoardcast (@PathVariable String title) throws IOException {
        MyWebSocketHandler.boardcast(title);

        return "{\"message\":\"success\"}";
    }
```

**Page**

[websocket2](https://github.com/yingsunnn/backend-features/blob/master/src/main/resources/static/websocket2.html)

```javascript
websocket = new SockJS("https://127.0.0.1:8443/ws2/post/10000/sockjs");

websocket.onopen = function (event) {
    console.log(":) WebSocket:已连接");
    console.log(event);
};
websocket.onmessage = function (event) {
    var data = JSON.parse(event.data);
    console.log(":) WebSocket:收到一条消息", data);
};
websocket.onerror = function (event) {
    console.log(":) WebSocket:发生错误 ");
    console.log(event);
};
websocket.onclose = function (event) {
    console.log(":) WebSocket:已关闭");
    console.log(event);
}
        
function sendPost() {
    var v = $("#post").val();
    if (v == "") {
        return;
    } else {
        var data = {};
        data["userId"] = "1";
        data["title"] = v;
        data["summary"] = "This is a summary.";
        websocket.send(JSON.stringify(data));
    }
}        
```

## Cors filter

[CorsFilter](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/filter/CorsFilter.java)

```java
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.addHeader("Access-Control-Allow-Credentials", "true");

        if (isPreFlightRequest(httpRequest)) {
            // CORS "pre-flight" request
            httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Origin,Content-Type,Accept,access_token,user_id");
            return;
        }

        chain.doFilter(request, response);
    }
    
    private boolean isCorsRequest(HttpServletRequest request) {
        return (request.getHeader(HttpHeaders.ORIGIN) != null);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return (isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) &&
                request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
    }

```

## Exception handler

[ExceptionsHandler](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/Exceptions/ExceptionsHandler.java)

```java
@ControllerAdvice
@ResponseBody
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        e.printStackTrace();
        return CommonJSONUtils.getErrorJson("0", "Internal exception. " + getMessage(e));
    }
}
```

## Get IP address

[IPAddressUtils](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/utils/IPAddressUtils.java)

```java
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
```
## MySQL 

**Configuration**

[MySQLDatasourceConfig](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/mysql/MySQLDatasourceConfig.java)

```java
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(CLASS_NAME);
        dataSource.setUsername(mySQLUsername);
        dataSource.setPassword(mySQLPassword);
        dataSource.setUrl(mySQLURL);
        return dataSource;
    }

    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.afterPropertiesSet();
        return jdbcTemplate;
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
```

**Operation**

[MySQLController](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/mysql/MySQLController.java)

```java
StringBuilder sql = new StringBuilder();
MapSqlParameterSource params = new MapSqlParameterSource();

sql.append("SELECT ");
sql.append("  u.user_id, ");
sql.append("  u.username, ");
sql.append("  u.profile_picture, ");
sql.append("  u.bio, ");
sql.append("  u.gender, ");
sql.append("  u.region, ");
sql.append("  u.role, ");
sql.append("  u.birthday, ");
sql.append("  u.occupation, ");
sql.append("  u.user_status, ");
sql.append("  u.created_at, ");
sql.append("  u.updated_at ");
sql.append("FROM t_user u ");
sql.append("Where u.user_id = :userId");

params.addValue("userId", userId);
List<User> users = namedParameterJdbcTemplate.query(sql.toString(), params, new UserMapper());
```

**Mapper**

[MySQLController - UserMapper](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/mysql/MySQLController.java)

```java
    private class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setProfilePictureId(rs.getLong("profile_picture"));
            user.setBio(rs.getString("bio"));
            user.setGender(rs.getString("gender"));
            user.setRegionId(rs.getLong("region"));
            user.setRoleId(rs.getLong("role"));
            user.setBirthday(rs.getLong("birthday"));
            user.setOccupation(rs.getString("occupation"));
            user.setUserStatus(rs.getString("user_status"));
            user.setCreatedAt(rs.getLong("created_at"));
            user.setUpdatedAt(rs.getLong("updated_at"));
            return user;
        }
    }
```

## JPA

### Dependences

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.github.wenhao</groupId>
    <artifactId>jpa-spec</artifactId>
    <version>3.1.1</version>
</dependency>
```

### Configuration

```
spring.datasource.url= jdbc:mysql://localhost:3306/test_db?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
# Number of ms to wait before throwing an exception if no connection is available.
spring.datasource.tomcat.max-wait=10000
# Maximum number of active connections that can be allocated from this pool at the same time.
spring.datasource.tomcat.max-active=50
# Validate the connection before borrowing it from the pool.
spring.datasource.tomcat.test-on-borrow=true
```

### Entity

[UserProfile](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/jpa/UserProfile.java)

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "t_user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", length = 100)
    private String name;

    private String bio;
}
```

### Repository

[UserProfileRepository](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/jpa/UserProfileRepository.java)

```java
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {

    UserProfile findById (Long Id);
}
```

### Operations

[JPAController](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/jpa/JPAController.java)

```java
userProfileRepository.save(userProfile);
        return userProfile;
```

```java
Specification<UserProfile> specification = Specifications.<UserProfile>and()
                .eq(id != null, "id", id)
                .like(StringUtils.isNotBlank(name), "name", "%" + name + "%")
                .like(StringUtils.isNotBlank(bio), "bio", "%" + bio + "%")
                .build();

return userProfileRepository.findAll(specification, pageable);
```

### Many to Many
**Entities**

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "t_drug")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "drugs", fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;

}
```

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "t_prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_prescription_drug",
            joinColumns = {@JoinColumn(name = "prescription_id")},
            inverseJoinColumns = {@JoinColumn(name = "drug_id")})
    private List<Drug> drugs;

    @Column(name = "create_time")
    private ZonedDateTime createTime;
}
```

## ZonedDateTime
[DateTimeUtils](https://github.com/yingsunnn/backend-features/blob/master/src/main/java/ying/backend_features/utils/DateTimeUtils.java)
