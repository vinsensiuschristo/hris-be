package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Role;
import org.example.hris.domain.repository.RoleRepository;
import org.example.hris.infrastructure.persistence.entity.RoleEntity;
import org.example.hris.infrastructure.persistence.mapper.RoleMapper;
import org.example.hris.infrastructure.persistence.repository.RoleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRoleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Role save(Role role) {
        RoleEntity entity = roleMapper.toEntity(role);
        RoleEntity saved = jpaRoleRepository.save(entity);
        return roleMapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRoleRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return jpaRoleRepository.findAll()
                .stream()
                .map(roleMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRoleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findByNamaRole(String namaRole) {
        return jpaRoleRepository.findByNamaRole(namaRole)
                .map(roleMapper::toDomain);
    }
}