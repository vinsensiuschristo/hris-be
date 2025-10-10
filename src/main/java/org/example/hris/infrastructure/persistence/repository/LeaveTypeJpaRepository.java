package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LeaveTypeJpaRepository extends JpaRepository<LeaveTypeEntity, UUID> {
    Optional<LeaveTypeEntity> findByNamaJenis(String namaJenis);
}