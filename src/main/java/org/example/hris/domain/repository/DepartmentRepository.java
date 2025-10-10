package org.example.hris.domain.repository;

import org.example.hris.domain.model.Department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {
    Department save(Department department);

    List<Department> findAll();

    Optional<Department> findById(UUID id);

    void deleteById(UUID id);
}
