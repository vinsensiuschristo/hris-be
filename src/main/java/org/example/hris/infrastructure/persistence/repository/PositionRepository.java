package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PositionRepository extends JpaRepository<PositionEntity, UUID> {
}
