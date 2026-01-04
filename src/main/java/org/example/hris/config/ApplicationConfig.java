package org.example.hris.config;

import lombok.RequiredArgsConstructor;
import org.example.hris.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // Use UserJpaRepository which returns UserEntity (implements UserDetails)
    private final UserJpaRepository userJpaRepository;

    /**
     * Bean ini memberi tahu Spring Security cara mengambil data user.
     * UserEntity implements UserDetails jadi bisa langsung digunakan.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Bean ini mendefinisikan encoder password yang akan kita gunakan (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean ini adalah "penyedia" autentikasi.
     * Ini menggabungkan UserDetailsService (untuk ambil data) dan PasswordEncoder (untuk cek password).
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean ini diperlukan oleh AuthService untuk menjalankan proses autentikasi.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
