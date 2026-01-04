package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Role;
import org.example.hris.domain.model.User;
import org.example.hris.domain.repository.RoleRepository;
import org.example.hris.domain.repository.UserRepository;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.entity.RoleEntity;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.RoleJpaRepository;
import org.example.hris.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
    }

    @Transactional
    public User createUser(String username, String password, UUID roleId, UUID karyawanId) {
        // Check if username already exists
        userJpaRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("Username sudah digunakan");
        });

        // Get role entity
        RoleEntity roleEntity = roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role tidak ditemukan"));

        // Build user entity directly to properly set roles
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .roles(new HashSet<>(Set.of(roleEntity)))
                .build();

        // Set karyawan relationship if provided
        if (karyawanId != null) {
            EmployeeEntity employee = employeeJpaRepository.findById(karyawanId)
                    .orElseThrow(() -> new IllegalArgumentException("Karyawan tidak ditemukan"));
            userEntity.setKaryawan(employee);
        }

        // Save user with roles
        UserEntity savedEntity = userJpaRepository.save(userEntity);

        // Return domain model
        return User.builder()
                .id(savedEntity.getId())
                .username(savedEntity.getUsername())
                .passwordHash(savedEntity.getPasswordHash())
                .build();
    }

    @Transactional
    public User updateUser(UUID id, String username, String password, UUID roleId, UUID karyawanId) {
        UserEntity entity = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        if (username != null && !username.isBlank()) {
            // Check if new username is taken by another user
            userJpaRepository.findByUsername(username).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new IllegalArgumentException("Username sudah digunakan");
                }
            });
            entity.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            entity.setPasswordHash(passwordEncoder.encode(password));
        }

        // Update role if provided
        if (roleId != null) {
            RoleEntity roleEntity = roleJpaRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role tidak ditemukan"));
            entity.setRoles(new HashSet<>(Set.of(roleEntity)));
        }

        // Update karyawan relationship
        if (karyawanId != null) {
            EmployeeEntity employee = employeeJpaRepository.findById(karyawanId)
                    .orElseThrow(() -> new IllegalArgumentException("Karyawan tidak ditemukan"));
            entity.setKaryawan(employee);
        } else {
            entity.setKaryawan(null); // Allow unlinking
        }

        UserEntity savedEntity = userJpaRepository.save(entity);

        return User.builder()
                .id(savedEntity.getId())
                .username(savedEntity.getUsername())
                .passwordHash(savedEntity.getPasswordHash())
                .build();
    }

    public void deleteUser(UUID id) {
        getUserById(id); // Validate exists
        userRepository.deleteById(id);
    }
}
