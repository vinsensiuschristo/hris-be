package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Position;
import org.example.hris.domain.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Position getPositionById(UUID id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    public Position updatePosition(UUID id, Position updated) {
        Position existing = getPositionById(id);
        existing.setNamaJabatan(updated.getNamaJabatan());
        return positionRepository.save(existing);
    }

    public void deletePosition(UUID id) {
        positionRepository.deleteById(id);
    }
}
