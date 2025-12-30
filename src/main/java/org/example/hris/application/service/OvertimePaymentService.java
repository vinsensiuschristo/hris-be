package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Employee;
import org.example.hris.domain.model.OvertimePayment;
import org.example.hris.domain.model.OvertimeRequest;
import org.example.hris.domain.model.RequestStatus;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.domain.repository.OvertimePaymentRepository;
import org.example.hris.domain.repository.OvertimeRequestRepository;
import org.example.hris.infrastructure.persistence.entity.RequestStatusEntity;
import org.example.hris.infrastructure.persistence.mapper.RequestStatusMapper;
import org.example.hris.infrastructure.persistence.repository.RequestStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OvertimePaymentService {

    private final OvertimePaymentRepository overtimePaymentRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper;

    private static final String STATUS_DISETUJUI = "DISETUJUI";
    private static final String STATUS_BELUM_DIPROSES = "BELUM_DIPROSES";
    private static final String STATUS_SEDANG_DIPROSES = "SEDANG_DIPROSES";
    private static final String STATUS_SUDAH_DICAIRKAN = "SUDAH_DICAIRKAN";

    public List<OvertimePayment> getAllPayments() {
        return overtimePaymentRepository.findAll();
    }

    public OvertimePayment getPaymentById(UUID id) {
        return overtimePaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime payment not found with id: " + id));
    }

    public List<OvertimePayment> getPendingPayments() {
        return overtimePaymentRepository.findByStatusNamaStatus(STATUS_BELUM_DIPROSES);
    }

    public List<OvertimePayment> getProcessingPayments() {
        return overtimePaymentRepository.findByStatusNamaStatus(STATUS_SEDANG_DIPROSES);
    }

    public List<OvertimePayment> getCompletedPayments() {
        return overtimePaymentRepository.findByStatusNamaStatus(STATUS_SUDAH_DICAIRKAN);
    }

    @Transactional
    public OvertimePayment createPayment(UUID overtimeRequestId, UUID financeId) {
        // Get overtime request
        OvertimeRequest overtimeRequest = overtimeRequestRepository.findById(overtimeRequestId)
                .orElseThrow(() -> new RuntimeException("Overtime request not found with id: " + overtimeRequestId));

        // Validate overtime request is approved
        if (!STATUS_DISETUJUI.equals(overtimeRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Overtime request must be approved before creating payment. Current status: " + 
                    overtimeRequest.getStatus().getNamaStatus());
        }

        // Check if payment already exists
        if (overtimePaymentRepository.findByPengajuanLemburId(overtimeRequestId).isPresent()) {
            throw new RuntimeException("Payment already exists for this overtime request");
        }

        // Get finance employee if provided
        Employee finance = null;
        if (financeId != null) {
            finance = employeeRepository.findById(financeId)
                    .orElseThrow(() -> new RuntimeException("Finance employee not found with id: " + financeId));
        }

        // Get initial status
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_BELUM_DIPROSES)
                .orElseThrow(() -> new RuntimeException("Status BELUM_DIPROSES tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        // Create payment
        OvertimePayment payment = OvertimePayment.builder()
                .pengajuanLembur(overtimeRequest)
                .finance(finance)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return overtimePaymentRepository.save(payment);
    }

    @Transactional
    public OvertimePayment processPayment(UUID paymentId, UUID financeId) {
        OvertimePayment payment = getPaymentById(paymentId);

        // Validate current status
        if (!STATUS_BELUM_DIPROSES.equals(payment.getStatus().getNamaStatus())) {
            throw new RuntimeException("Payment is not in pending status");
        }

        // Set finance employee
        if (financeId != null) {
            Employee finance = employeeRepository.findById(financeId)
                    .orElseThrow(() -> new RuntimeException("Finance employee not found with id: " + financeId));
            payment.setFinance(finance);
        }

        // Update status to processing
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_SEDANG_DIPROSES)
                .orElseThrow(() -> new RuntimeException("Status SEDANG_DIPROSES tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());

        return overtimePaymentRepository.save(payment);
    }

    @Transactional
    public OvertimePayment markAsPaid(UUID paymentId) {
        OvertimePayment payment = getPaymentById(paymentId);

        // Validate current status
        String currentStatus = payment.getStatus().getNamaStatus();
        if (!STATUS_BELUM_DIPROSES.equals(currentStatus) && !STATUS_SEDANG_DIPROSES.equals(currentStatus)) {
            throw new RuntimeException("Payment must be in pending or processing status");
        }

        // Update status to paid
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_SUDAH_DICAIRKAN)
                .orElseThrow(() -> new RuntimeException("Status SUDAH_DICAIRKAN tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        payment.setStatus(status);
        payment.setTglPembayaran(LocalDate.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return overtimePaymentRepository.save(payment);
    }
}
