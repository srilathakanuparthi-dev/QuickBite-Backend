
package quickbite_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import quickbite_backend.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            // JWT authentication is stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

            	    .requestMatchers(
            	        HttpMethod.POST,
            	        "/api/users/signup",
            	        "/api/users/login",
            	        "/api/auth/refresh",
            	        "/api/auth/logout"
            	    ).permitAll()

            	    .requestMatchers(
            	        HttpMethod.GET,
            	        "/api/foods",
            	        "/api/foods/**"
            	    ).permitAll()

            	    .requestMatchers(
            	        HttpMethod.PUT,
            	        "/api/orders/*/status"
            	    ).hasRole("ADMIN")

            	    .requestMatchers("/actuator/**").permitAll()

            	    .anyRequest().authenticated()
            	)

            // Connect JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
