package es.in2.cloud.licensing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import es.in2.cloud.licensing.config.ServiceConfig;

@EnableCircuitBreaker
@EnableBinding(Sink.class)
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class Application {
	
    @Autowired
    private ServiceConfig serviceConfig;
    
    private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return requestTemplate -> {
//            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
//            if (details instanceof OAuth2AuthenticationDetails) {
//                String tokenValue = ((OAuth2AuthenticationDetails) details).getTokenValue();
//                requestTemplate.header("Authorization", "bearer " + tokenValue);
//            }
//        };
//    }
	
	@Bean
    @LoadBalanced //Fix for 'UnknownHostException: zuul'
    public OAuth2RestOperations oauth2RestTemplate(
    		OAuth2ProtectedResourceDetails resource, 
    		OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }
	
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(serviceConfig.getRedisServer());
        redisStandaloneConfiguration.setPort(serviceConfig.getRedisPort());
//        redisStandaloneConfiguration.setDatabase(0);
//        redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));

        JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
//        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return jedisConFactory;
    }
	

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    
}
