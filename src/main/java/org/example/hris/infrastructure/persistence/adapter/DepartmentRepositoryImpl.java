package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Department;
import org.example.hris.domain.repository.DepartmentRepository;
import org.example.hris.infrastructure.persistence.mapper.DepartmentMapper;
import org.example.hris.infrastructure.persistence.repository.DepartmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentJpaRepository departmentJpaRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Department save(Department department) {
        return departmentMapper.toDomain(departmentJpaRepository.save(departmentMapper.toEntity(department)));
    }

    @Override
    public List<Department> findAll() {
        return departmentJpaRepository.findAll()
                .stream()
                .map(departmentMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return departmentJpaRepository.findById(id)
                .map(departmentMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        departmentJpaRepository.deleteById(id);
    }
}
