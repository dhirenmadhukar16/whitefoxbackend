package com.example.whitefox.config.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                http
    .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/store-ops/**",
                                "/api/payments/**",
                                "/api/invoices/**",
                                "/api/reports/**",
                                "/api/pickup-bills/**",
                                "/api/tracking/**",
                                "/api/rider/**",
                                "/api/customer-bookings/**",
                                "/api/customer-app/**",
                                "/api/customer-bookings/**",
                                "/api/customer-addresses/**",
                                "/api/customer-updates/**",
                                "/api/customer-reviews/**",
                                "/api/customer-app/**",
                                "/api/customer-bookings/**",
                                "/api/customer-addresses/**",
                                "/api/customer-updates/**",
                                "/api/customer-reviews/**",
                                "/api/admin-panel/**",
                                "/api/admin-settings/**",
                                "/api/notifications/**",
                                "/api/truck-logistics/**",
                                "/api/truck-logistics/**",
                                "/api/hq/**",
                                "/api/store-app/**",
                                "/api/customers/**",
                                "/api/admin/catalog/**",
                                "/api/orders/**"

                        )
                        .permitAll()

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/admin-panel/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**")
                        .hasAnyRole("USER", "ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


}