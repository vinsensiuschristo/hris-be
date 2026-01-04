package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.LeaveRequest;
import org.example.hris.domain.repository.LeaveRequestRepository;
import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.example.hris.infrastructure.persistence.mapper.LeaveRequestMapper;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.LeaveRequestJpaRepository;
import org.example.hris.infrastructure.persistence.repository.LeaveTypeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.RequestStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private final LeaveRequestJpaRepository leaveRequestJpaRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final LeaveTypeJpaRepository leaveTypeJpaRepository;
    private final RequestStatusRepository requestStatusRepository;

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        LeaveRequestEntity entity = leaveRequestMapper.toEntity(leaveRequest);
        
        // Set karyawan entity (ignored by mapper)
        if (leaveRequest.getKaryawan() != null && leaveRequest.getKaryawan().getId() != null) {
            employeeJpaRepository.findById(leaveRequest.getKaryawan().getId())
                    .ifPresent(entity::setKaryawan);
        }
        
        // Set jenisCuti entity (ignored by mapper)
        if (leaveRequest.getJenisCuti() != null && leaveRequest.getJenisCuti().getId() != null) {
            leaveTypeJpaRepository.findById(leaveRequest.getJenisCuti().getId())
                    .ifPresent(entity::setJenisCuti);
        }
        
        // Set status entity (ignored by mapper)
        if (leaveRequest.getStatus() != null && leaveRequest.getStatus().getId() != null) {
            requestStatusRepository.findById(leaveRequest.getStatus().getId())
                    .ifPresent(entity::setStatus);
        }
        
        return leaveRequestMapper.toDomain(leaveRequestJpaRepository.save(entity));
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
