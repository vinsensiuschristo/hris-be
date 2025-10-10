package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.LeaveType;
import org.example.hris.domain.repository.LeaveTypeRepository;
import org.example.hris.infrastructure.persistence.entity.LeaveTypeEntity;
import org.example.hris.infrastructure.persistence.mapper.LeaveTypeMapper;
import org.example.hris.infrastructure.persistence.repository.LeaveTypeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveTypeRepositoryImpl implements LeaveTypeRepository {

    private final LeaveTypeJpaRepository jpaLeaveTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;

    @Override
    public LeaveType save(LeaveType leaveType) {
        LeaveTypeEntity entity = leaveTypeMapper.toEntity(leaveType);
        LeaveTypeEntity saved = jpaLeaveTypeRepository.save(entity);
        return leaveTypeMapper.toDomain(saved);
    }

    @Override
    public Optional<LeaveType> findById(UUID id) {
        return jpaLeaveTypeRepository.findById(id)
                .map(leaveTypeMapper::toDomain);
    }

    @Override
    public List<LeaveType> findAll() {
        return jpaLeaveTypeRepository.findAll()
                .stream()
                .map(leaveTypeMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaLeaveTypeRepository.deleteById(id);
    }

    @Override
    public Optional<LeaveType> findByNamaJenis(String namaJenis) {
        return jpaLeaveTypeRepository.findByNamaJenis(namaJenis)
                .map(leaveTypeMapper::toDomain);
    }
}
