package es.in2.cloud.auth.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	private final Log logger = LogFactory.getLog(WebSecurityConfigurer.class);
	
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	PasswordEncoder encoder =
    		     PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	
    	String userPass = encoder.encode("password1"); //TODO do not keep unencrypted passwords under version control
    	logger.info("User pass hash=" + userPass);
    	//result similar to: {bcrypt}$2a$10$arfMouXG4dLAF12KeJFWP.0z9P2vgQtmlAOtlced2x2.XBzpE6OkK
    	
    	String adminPass = encoder.encode("password2");
    	logger.info("Admin pass hash=" + adminPass);
    	    	
        auth
	        .inMemoryAuthentication()
	        .passwordEncoder(encoder)
	        .withUser("john.carnell").password(userPass).roles("USER")
	        .and()
	        .withUser("william.woodward")
	        .password("{bcrypt}$2a$10$1zjqCF4h3QBM8Tvm7bo8tuaPGkhmuVkNwJLGxxtWCS6Tc8C472Opa")
	        .roles("USER", "ADMIN");
    }
}
