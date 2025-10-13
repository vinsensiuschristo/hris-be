package org.example.hris.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ⚙️ --- BAGIAN UNTUK DEVELOPMENT (sementara) ---
        boolean isDevelopment = true; // <--- GANTI KE false SAAT PRODUKSI

        if (isDevelopment) {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"
                            ).permitAll()
                            .requestMatchers("/api/**").permitAll() // Semua endpoint bebas diakses
                            .anyRequest().permitAll()
                    )
                    .httpBasic(Customizer.withDefaults())
                    .formLogin(Customizer.withDefaults());
        }

        // 🛡️ --- BAGIAN UNTUK PRODUCTION (aktifkan nanti) ---
        else {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"
                            ).permitAll()
                            // API perlu autentikasi
                            .requestMatchers("/api/auth/**").permitAll() // login/register tetap bebas
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().authenticated()
                    )
                    // Gunakan JWT (stateless)
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .httpBasic(Customizer.withDefaults())
                    .formLogin(Customizer.withDefaults());
        }

        return http.build();
    }
}

