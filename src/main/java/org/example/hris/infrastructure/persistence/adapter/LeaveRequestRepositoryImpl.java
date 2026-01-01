package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.LeaveRequest;
import org.example.hris.domain.repository.LeaveRequestRepository;
import org.example.hris.infrastructure.persistence.mapper.LeaveRequestMapper;
import org.example.hris.infrastructure.persistence.repository.LeaveRequestJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private final LeaveRequestJpaRepository leaveRequestJpaRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        return leaveRequestMapper.toDomain(
                leaveRequestJpaRepository.save(leaveRequestMapper.toEntity(leaveRequest))
        );
    }

    @Override
    public List<LeaveRequest> findAll() {
        return leaveRequestJpaRepository.findAll()
                .stream()
                .map(leaveRequestMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<LeaveRequest> findById(UUID id) {
        return leaveRequestJpaRepository.findById(id)
                .map(leaveRequestMapper::toDomain);
    }

    @Override
    public List<LeaveRequest> findByKaryawanId(UUID karyawanId) {
        return leaveRequestJpaRepository.findByKaryawan_Id(karyawanId)
                .stream()
                .map(leaveRequestMapper::toDomain)
                .toList();
    }

    @Override
    public List<LeaveRequest> findByStatusNamaStatus(String status) {
        return leaveRequestJpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getStatus() != null && 
                        status.equals(entity.getStatus().getNamaStatus()))
                .map(leaveRequestMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        leaveRequestJpaRepository.deleteById(id);
    }
}
