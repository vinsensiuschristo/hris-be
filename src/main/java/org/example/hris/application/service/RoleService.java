package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Role;
import org.example.hris.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(Role role) {
        roleRepository.findByNamaRole(role.getNamaRole()).ifPresent(r -> {
            throw new IllegalArgumentException("Nama role sudah digunakan");
        });
        return roleRepository.save(role);
    }

    public Role update(UUID id, Role role) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role tidak ditemukan"));

        existing.setNamaRole(role.getNamaRole());

        return roleRepository.save(existing);
    }

    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }

    public Role getById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role tidak ditemukan"));
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
