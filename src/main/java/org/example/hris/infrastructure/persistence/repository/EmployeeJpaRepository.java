package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("employeeJpaRepository")
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByNik(String nik);

    Optional<EmployeeEntity> findByEmail(String email);
}
