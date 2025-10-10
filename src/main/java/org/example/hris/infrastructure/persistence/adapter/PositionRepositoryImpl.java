package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Position;
import org.example.hris.domain.repository.PositionRepository;
import org.example.hris.infrastructure.persistence.mapper.PositionMapper;
import org.example.hris.infrastructure.persistence.repository.PositionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl implements PositionRepository {

    private final PositionJpaRepository jpaRepository;
    private final PositionMapper mapper;

    @Override
    public Position save(Position position) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(position)));
    }

    @Override
    public List<Position> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Position> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
