package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.WorkHoursSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkHoursSettingsJpaRepository extends JpaRepository<WorkHoursSettingsEntity, UUID> {
    Optional<WorkHoursSettingsEntity> findByIsActiveTrue();
}
