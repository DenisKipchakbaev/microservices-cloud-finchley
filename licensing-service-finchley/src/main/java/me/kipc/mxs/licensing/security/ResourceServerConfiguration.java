package me.kipc.mxs.licensing.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
	
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http
    		.authorizeRequests()
//    			.antMatchers("/**") //open /proxy.stream for hystrix dashboard
//    				.permitAll()
    			.antMatchers("/actuator/**") //TODO secure actuator
    				.permitAll()
//    			.antMatchers("/webjars/**")
//    				.permitAll()
    			
                .antMatchers("/v1/organizations/**")
                	.hasRole("ADMIN")
                .anyRequest()
                	.authenticated();
    }
    
}
