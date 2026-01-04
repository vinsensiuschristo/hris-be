package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.OvertimeRequest;
import org.example.hris.domain.repository.OvertimeRequestRepository;
import org.example.hris.infrastructure.persistence.entity.OvertimeRequestEntity;
import org.example.hris.infrastructure.persistence.mapper.OvertimeRequestMapper;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.OvertimeRequestJpaRepository;
import org.example.hris.infrastructure.persistence.repository.RequestStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OvertimeRequestRepositoryImpl implements OvertimeRequestRepository {

    private final OvertimeRequestJpaRepository overtimeRequestJpaRepository;
    private final OvertimeRequestMapper overtimeRequestMapper;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final RequestStatusRepository requestStatusRepository;

    @Override
    public OvertimeRequest save(OvertimeRequest overtimeRequest) {
        OvertimeRequestEntity entity = overtimeRequestMapper.toEntity(overtimeRequest);
        
        // Set karyawan entity (ignored by mapper)
        if (overtimeRequest.getKaryawan() != null && overtimeRequest.getKaryawan().getId() != null) {
            employeeJpaRepository.findById(overtimeRequest.getKaryawan().getId())
                    .ifPresent(entity::setKaryawan);
        }
        
        // Set status entity (ignored by mapper)
        if (overtimeRequest.getStatus() != null && overtimeRequest.getStatus().getId() != null) {
            requestStatusRepository.findById(overtimeRequest.getStatus().getId())
                    .ifPresent(entity::setStatus);
        }
        
        return overtimeRequestMapper.toDomain(overtimeRequestJpaRepository.save(entity));
    }

    @Override
    public List<OvertimeRequest> findAll() {
        return overtimeRequestJpaRepository.findAll()
                .stream()
                .map(overtimeRequestMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OvertimeRequest> findById(UUID id) {
        return overtimeRequestJpaRepository.findById(id)
                .map(overtimeRequestMapper::toDomain);
    }

    @Override
    public List<OvertimeRequest> findByKaryawanId(UUID karyawanId) {
        return overtimeRequestJpaRepository.findByKaryawan_Id(karyawanId)
                .stream()
                .map(overtimeRequestMapper::toDomain)
                .toList();
    }

    @Override
    public List<OvertimeRequest> findByStatusNamaStatus(String status) {
        return overtimeRequestJpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getStatus() != null &&
                        status.equals(entity.getStatus().getNamaStatus()))
                .map(overtimeRequestMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        overtimeRequestJpaRepository.deleteById(id);
    }
}
