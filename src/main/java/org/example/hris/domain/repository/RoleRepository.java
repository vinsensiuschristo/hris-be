package org.example.hris.domain.repository;

import org.example.hris.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);

    Optional<Role> findById(UUID id);

    List<Role> findAll();

    void deleteById(UUID id);

    Optional<Role> findByNamaRole(String namaRole);
}
