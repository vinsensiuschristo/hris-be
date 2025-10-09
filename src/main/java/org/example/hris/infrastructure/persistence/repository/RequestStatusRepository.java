package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.RequestStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RequestStatusRepository extends JpaRepository<RequestStatusEntity, UUID> {
    Optional<RequestStatusEntity> findByNamaStatus(String namaStatus);
}