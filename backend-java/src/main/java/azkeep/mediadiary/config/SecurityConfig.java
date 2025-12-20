package azkeep.mediadiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(
                        "/",
                        "/api/entries/**",
                        "/ui/**",
                        "/auth/**",
                        "/error",
                        "/images/**",
                        "/css/**",
                        "/static/**",
                        "/manifest.json",
                        "/favicon.ico")
                        .permitAll();
                    req.anyRequest().authenticated();
                })
//                .formLogin(Customizer.withDefaults())
                .build();
    }
}
