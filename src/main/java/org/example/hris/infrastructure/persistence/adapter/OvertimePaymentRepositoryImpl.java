package org.example.hris.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.OvertimePayment;
import org.example.hris.domain.repository.OvertimePaymentRepository;
import org.example.hris.infrastructure.persistence.mapper.OvertimePaymentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OvertimePaymentRepositoryImpl implements OvertimePaymentRepository {

    private final org.example.hris.infrastructure.persistence.repository.OvertimePaymentRepository overtimePaymentJpaRepository;
    private final OvertimePaymentMapper overtimePaymentMapper;

    @Override
    public OvertimePayment save(OvertimePayment overtimePayment) {
        return overtimePaymentMapper.toDomain(
                overtimePaymentJpaRepository.save(overtimePaymentMapper.toEntity(overtimePayment))
        );
    }

    @Override
    public List<OvertimePayment> findAll() {
        return overtimePaymentJpaRepository.findAll()
                .stream()
                .map(overtimePaymentMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OvertimePayment> findById(UUID id) {
        return overtimePaymentJpaRepository.findById(id)
                .map(overtimePaymentMapper::toDomain);
    }

    @Override
    public Optional<OvertimePayment> findByPengajuanLemburId(UUID pengajuanLemburId) {
        return overtimePaymentJpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getPengajuanLembur() != null &&
                        pengajuanLemburId.equals(entity.getPengajuanLembur().getId()))
                .findFirst()
                .map(overtimePaymentMapper::toDomain);
    }

    @Override
    public List<OvertimePayment> findByStatusNamaStatus(String status) {
        return overtimePaymentJpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getStatus() != null &&
                        status.equals(entity.getStatus().getNamaStatus()))
                .map(overtimePaymentMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        overtimePaymentJpaRepository.deleteById(id);
    }
}
