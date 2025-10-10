package org.example.hris.domain.repository;

import org.example.hris.domain.model.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository {
    Position save(Position position);

    List<Position> findAll();

    Optional<Position> findById(UUID id);

    void deleteById(UUID id);
}
