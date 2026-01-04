package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.auth.AuthResponse;
import org.example.hris.application.dto.auth.LoginRequest;
import org.example.hris.application.dto.auth.RegisterRequest;
import org.example.hris.infrastructure.persistence.entity.RoleEntity;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.repository.RoleJpaRepository;
import org.example.hris.infrastructure.persistence.repository.UserRepository;
import org.example.hris.infrastructure.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleJpaRepository roleJpaRepository; // Pastikan Anda sudah membuat repo ini
    private final PasswordEncoder passwordEncoder;     // Dari ApplicationConfig
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // Dari ApplicationConfig
    private final UserDetailsService userDetailsService; // Dari ApplicationConfig

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Cek jika user sudah ada
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        // 2. Cari role default, misal "KARYAWAN"
        // PENTING: Pastikan role "KARYAWAN" ada di tabel 'roles' database Anda
        RoleEntity defaultRole = roleJpaRepository.findByNamaRole("KARYAWAN")
                .orElseThrow(() -> new RuntimeException("Default role 'KARYAWAN' not found. Please seed the database."));

        // 3. Buat UserEntity baru
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(defaultRole))
                // Anda bisa tambahkan link ke EmployeeEntity di sini jika perlu
                .build();

        // 4. Simpan user baru
        userRepository.save(user);

        // 5. Generate token untuk user yang baru register
        UserDetails userDetails = user; // UserEntity Anda sudah implement UserDetails
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password length: " + (request.getPassword() != null ? request.getPassword().length() : 0));
        
        try {
            // 1. Autentikasi user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            System.out.println("Authentication successful!");

            // 2. Jika autentikasi berhasil
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // 3. Generate token
            String jwtToken = jwtService.generateToken(userDetails);
            System.out.println("Token generated successfully");

            return AuthResponse.builder()
                    .token(jwtToken)
                    .username(userDetails.getUsername())
                    .build();
        } catch (Exception e) {
            System.out.println("Authentication FAILED: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}