package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByNik(String nik);

    Optional<EmployeeEntity> findByEmail(String email);
}
