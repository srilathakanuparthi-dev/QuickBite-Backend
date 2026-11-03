
package quickbite_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

    	http
        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(
            	        HttpMethod.POST,
            	        "/api/users/signup",
            	        "/api/users/login"
            	    ).permitAll()
            	    .requestMatchers(
            	        HttpMethod.GET,
            	        "/api/foods"
            	    ).permitAll()
            	    .requestMatchers(
            	        HttpMethod.POST,
            	        "/api/foods"
            	    ).permitAll()
            	    .requestMatchers(
            	    	    HttpMethod.PUT,
            	    	    "/api/foods/**"
            	    	).permitAll()
            	    .requestMatchers(
            	    	    HttpMethod.DELETE,
            	    	    "/api/foods/**"
            	    	).permitAll()
            	    .requestMatchers("/api/cart/**", "/api/orders/**").permitAll()
            	    .anyRequest().authenticated()
            	);
        return http.build();
    }
}
