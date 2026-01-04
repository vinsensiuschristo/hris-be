package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.role.RoleCreateRequest;
import org.example.hris.application.dto.role.RoleResponse;
import org.example.hris.application.dto.role.RoleUpdateRequest;
import org.example.hris.application.service.RoleService;
import org.example.hris.domain.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleCreateRequest request) {
        Role role = Role.builder()
                .id(UUID.randomUUID())
                .namaRole(request.getNamaRole())
                .build();

        Role created = roleService.create(role);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> findAll() {
        List<RoleResponse> responses = roleService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable UUID id) {
        Role role = roleService.getById(id);
        return ResponseEntity.ok(toResponse(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        Role updated = Role.builder()
                .namaRole(request.getNamaRole())
                .build();

        Role saved = roleService.update(id, updated);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .namaRole(role.getNamaRole())
                .build();
    }
}