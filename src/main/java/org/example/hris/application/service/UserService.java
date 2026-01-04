package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Role;
import org.example.hris.domain.model.User;
import org.example.hris.domain.repository.RoleRepository;
import org.example.hris.domain.repository.UserRepository;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final EmployeeJpaRepository employeeJpaRepository;

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
        userRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("Username sudah digunakan");
        });

        // Validate role exists
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role tidak ditemukan"));

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .build();

        User savedUser = userRepository.save(user);
        
        // Set karyawan relationship if provided
        if (karyawanId != null) {
            UserEntity entity = userJpaRepository.findById(savedUser.getId()).orElseThrow();
            EmployeeEntity employee = employeeJpaRepository.findById(karyawanId)
                    .orElseThrow(() -> new IllegalArgumentException("Karyawan tidak ditemukan"));
            entity.setKaryawan(employee);
            userJpaRepository.save(entity);
        }

        return savedUser;
    }

    @Transactional
    public User updateUser(UUID id, String username, String password, UUID roleId, UUID karyawanId) {
        User existing = getUserById(id);

        if (username != null && !username.isBlank()) {
            // Check if new username is taken by another user
            userRepository.findByUsername(username).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new IllegalArgumentException("Username sudah digunakan");
                }
            });
            existing.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(password));
        }

        User savedUser = userRepository.save(existing);
        
        // Update karyawan relationship
        UserEntity entity = userJpaRepository.findById(id).orElseThrow();
        if (karyawanId != null) {
            EmployeeEntity employee = employeeJpaRepository.findById(karyawanId)
                    .orElseThrow(() -> new IllegalArgumentException("Karyawan tidak ditemukan"));
            entity.setKaryawan(employee);
        } else {
            entity.setKaryawan(null); // Allow unlinking
        }
        userJpaRepository.save(entity);

        return savedUser;
    }

    public void deleteUser(UUID id) {
        getUserById(id); // Validate exists
        userRepository.deleteById(id);
    }
}
