package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.auth.AuthResponse;
import org.example.hris.application.dto.auth.LoginRequest;
import org.example.hris.application.dto.auth.RegisterRequest;
import org.example.hris.application.dto.role.RoleResponse;
import org.example.hris.application.dto.user.UserResponse;
import org.example.hris.infrastructure.persistence.entity.RoleEntity;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.repository.RoleJpaRepository;
import org.example.hris.infrastructure.persistence.repository.UserJpaRepository;
import org.example.hris.infrastructure.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userJpaRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        RoleEntity defaultRole = roleJpaRepository.findByNamaRole("KARYAWAN")
                .orElseThrow(() -> new RuntimeException("Default role 'KARYAWAN' not found. Please seed the database."));

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(defaultRole))
                .build();

        userJpaRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .user(buildUserResponse(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserEntity user = userJpaRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .username(user.getUsername())
                    .user(buildUserResponse(user))
                    .build();
        } catch (Exception e) {
            System.out.println("Authentication FAILED: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            throw e;
        }
    }

    private UserResponse buildUserResponse(UserEntity user) {
        List<RoleResponse> roleResponses = user.getRoles().stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .namaRole(role.getNamaRole())
                        .build())
                .collect(Collectors.toList());

        UserResponse.KaryawanInfo karyawanInfo = null;
        if (user.getKaryawan() != null) {
            karyawanInfo = UserResponse.KaryawanInfo.builder()
                    .id(user.getKaryawan().getId())
                    .nama(user.getKaryawan().getNama())
                    .nik(user.getKaryawan().getNik())
                    .build();
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(roleResponses)
                .karyawan(karyawanInfo)
                .build();
    }
}
