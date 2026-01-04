package org.example.hris.application.service;

import org.example.hris.domain.model.Employee;
import org.example.hris.domain.model.OvertimeRequest;
import org.example.hris.domain.model.RequestStatus;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.domain.repository.OvertimeRequestRepository;
import org.example.hris.infrastructure.persistence.entity.RequestStatusEntity;
import org.example.hris.infrastructure.persistence.mapper.RequestStatusMapper;
import org.example.hris.infrastructure.persistence.repository.RequestStatusRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class OvertimeRequestService {

    private final OvertimeRequestRepository overtimeRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper;

    public OvertimeRequestService(
            @Lazy OvertimeRequestRepository overtimeRequestRepository,
            @Lazy EmployeeRepository employeeRepository,
            RequestStatusRepository requestStatusRepository,
            RequestStatusMapper requestStatusMapper) {
        this.overtimeRequestRepository = overtimeRequestRepository;
        this.employeeRepository = employeeRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.requestStatusMapper = requestStatusMapper;
    }

    private static final String STATUS_MENUNGGU = "MENUNGGU_PERSETUJUAN";
    private static final String STATUS_MENUNGGU_REIMBURSE = "MENUNGGU_REIMBURSE";
    private static final String STATUS_DIBAYAR = "DIBAYAR";
    private static final String STATUS_DISETUJUI = "DISETUJUI";
    private static final String STATUS_DITOLAK = "DITOLAK";

    // Overtime rate per hour (can be moved to configuration)
    private static final BigDecimal OVERTIME_RATE_PER_HOUR = new BigDecimal("50000");

    public List<OvertimeRequest> getAllOvertimeRequests() {
        return overtimeRequestRepository.findAll();
    }

    public OvertimeRequest getOvertimeRequestById(UUID id) {
        return overtimeRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime request not found with id: " + id));
    }

    public List<OvertimeRequest> getOvertimeRequestsByKaryawanId(UUID karyawanId) {
        return overtimeRequestRepository.findByKaryawanId(karyawanId);
    }

    public List<OvertimeRequest> getPendingOvertimeRequests() {
        return overtimeRequestRepository.findByStatusNamaStatus(STATUS_MENUNGGU);
    }

    public List<OvertimeRequest> getApprovedOvertimeRequests() {
        return overtimeRequestRepository.findByStatusNamaStatus(STATUS_MENUNGGU_REIMBURSE);
    }

    public List<OvertimeRequest> getPaidOvertimeRequests() {
        return overtimeRequestRepository.findByStatusNamaStatus(STATUS_DIBAYAR);
    }

    @Transactional
    public OvertimeRequest createOvertimeRequest(UUID karyawanId, LocalDate tglLembur, LocalTime jamMulai, LocalTime jamSelesai) {
        // Validate times
        if (jamMulai.isAfter(jamSelesai)) {
            throw new RuntimeException("Jam mulai tidak boleh setelah jam selesai");
        }

        // Get employee
        Employee karyawan = employeeRepository.findById(karyawanId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + karyawanId));

        // Calculate duration in minutes
        long durasiMenit = ChronoUnit.MINUTES.between(jamMulai, jamSelesai);
        int durasiJam = (int) Math.ceil(durasiMenit / 60.0);

        // Calculate estimated cost
        BigDecimal estimasiBiaya = OVERTIME_RATE_PER_HOUR.multiply(BigDecimal.valueOf(durasiJam));

        // Get pending status
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_MENUNGGU)
                .orElseThrow(() -> new RuntimeException("Status MENUNGGU_PERSETUJUAN tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        // Create overtime request
        OvertimeRequest overtimeRequest = OvertimeRequest.builder()
                .karyawan(karyawan)
                .tglLembur(tglLembur)
                .jamMulai(jamMulai)
                .jamSelesai(jamSelesai)
                .durasi(durasiJam)
                .estimasiBiaya(estimasiBiaya)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return overtimeRequestRepository.save(overtimeRequest);
    }

    @Transactional
    public OvertimeRequest approveOvertimeRequest(UUID id) {
        OvertimeRequest overtimeRequest = getOvertimeRequestById(id);

        // Validate current status
        if (!STATUS_MENUNGGU.equals(overtimeRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Pengajuan lembur sudah diproses sebelumnya");
        }

        // Update status to MENUNGGU_REIMBURSE (approved, waiting for payment)
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_MENUNGGU_REIMBURSE)
                .orElseThrow(() -> new RuntimeException("Status MENUNGGU_REIMBURSE tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        overtimeRequest.setStatus(status);
        overtimeRequest.setUpdatedAt(LocalDateTime.now());

        return overtimeRequestRepository.save(overtimeRequest);
    }

    @Transactional
    public OvertimeRequest rejectOvertimeRequest(UUID id, String alasanPenolakan) {
        OvertimeRequest overtimeRequest = getOvertimeRequestById(id);

        // Validate current status
        if (!STATUS_MENUNGGU.equals(overtimeRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Pengajuan lembur sudah diproses sebelumnya");
        }

        // Update status to rejected
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_DITOLAK)
                .orElseThrow(() -> new RuntimeException("Status DITOLAK tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        overtimeRequest.setStatus(status);
        overtimeRequest.setAlasanPenolakan(alasanPenolakan);
        overtimeRequest.setUpdatedAt(LocalDateTime.now());

        return overtimeRequestRepository.save(overtimeRequest);
    }

    @Transactional
    public OvertimeRequest reimburseOvertimeRequest(UUID id) {
        OvertimeRequest overtimeRequest = getOvertimeRequestById(id);

        // Validate current status - must be MENUNGGU_REIMBURSE
        if (!STATUS_MENUNGGU_REIMBURSE.equals(overtimeRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Pengajuan lembur harus dalam status MENUNGGU_REIMBURSE untuk dapat dibayar. Status saat ini: " + 
                    overtimeRequest.getStatus().getNamaStatus());
        }

        // Update status to DIBAYAR
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_DIBAYAR)
                .orElseThrow(() -> new RuntimeException("Status DIBAYAR tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        overtimeRequest.setStatus(status);
        overtimeRequest.setUpdatedAt(LocalDateTime.now());

        return overtimeRequestRepository.save(overtimeRequest);
    }

    public BigDecimal calculateOvertimeCost(LocalTime jamMulai, LocalTime jamSelesai) {
        long durasiMenit = ChronoUnit.MINUTES.between(jamMulai, jamSelesai);
        int durasiJam = (int) Math.ceil(durasiMenit / 60.0);
        return OVERTIME_RATE_PER_HOUR.multiply(BigDecimal.valueOf(durasiJam));
    }
}

