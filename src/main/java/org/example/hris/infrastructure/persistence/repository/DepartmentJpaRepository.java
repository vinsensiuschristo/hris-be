package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, UUID> {
}
