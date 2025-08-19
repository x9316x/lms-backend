package com.example.lms.config;

import com.example.lms.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // H2 консоль и stateless
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Правила доступа
                .authorizeHttpRequests(auth -> auth
                        // Полностью открыто
                        .requestMatchers("/api/ping", "/api/auth/**", "/h2-console/**", "/h2/**").permitAll()
                        // Публичные GET'ы
                        .requestMatchers(HttpMethod.GET,
                                "/api/courses/**",
                                "/api/modules/**",
                                "/api/lessons/**"
                        ).permitAll()
                        // Админка
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Остальное — с аутентификацией
                        .anyRequest().authenticated()
                )

                // JWT фильтр
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
