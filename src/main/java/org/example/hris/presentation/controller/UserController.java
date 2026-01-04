package org.example.hris.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.role.RoleResponse;
import org.example.hris.application.dto.user.UserCreateRequest;
import org.example.hris.application.dto.user.UserResponse;
import org.example.hris.application.dto.user.UserUpdateRequest;
import org.example.hris.application.service.UserService;
import org.example.hris.domain.model.User;
import org.example.hris.infrastructure.persistence.entity.UserEntity;
import org.example.hris.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;
    private final UserJpaRepository userJpaRepository;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> responses = userJpaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserEntity user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        return ResponseEntity.ok(toResponse(user));
    }

    @PostMapping
    @Operation(summary = "Create new user")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        User created = userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getRoleId(),
                request.getKaryawanId()
        );
        // Fetch entity to get roles for response
        UserEntity entity = userJpaRepository.findById(created.getId())
                .orElseThrow();
        return ResponseEntity.ok(toResponse(entity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        User updated = userService.updateUser(
                id,
                request.getUsername(),
                request.getPassword(),
                request.getRoleId(),
                request.getKaryawanId()
        );
        UserEntity entity = userJpaRepository.findById(updated.getId())
                .orElseThrow();
        return ResponseEntity.ok(toResponse(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponse toResponse(UserEntity entity) {
        List<RoleResponse> roles = entity.getRoles().stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .namaRole(role.getNamaRole())
                        .build())
                .collect(Collectors.toList());

        UserResponse.KaryawanInfo karyawanInfo = null;
        if (entity.getKaryawan() != null) {
            karyawanInfo = UserResponse.KaryawanInfo.builder()
                    .id(entity.getKaryawan().getId())
                    .nama(entity.getKaryawan().getNama())
                    .nik(entity.getKaryawan().getNik())
                    .build();
        }

        return UserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .roles(roles)
                .karyawan(karyawanInfo)
                .build();
    }
}
