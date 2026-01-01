package org.example.hris.application.service;

import org.example.hris.domain.model.*;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.domain.repository.LeaveRequestRepository;
import org.example.hris.domain.repository.LeaveTypeRepository;
import org.example.hris.infrastructure.persistence.entity.RequestStatusEntity;
import org.example.hris.infrastructure.persistence.mapper.RequestStatusMapper;
import org.example.hris.infrastructure.persistence.repository.RequestStatusRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final RequestStatusMapper requestStatusMapper;
    private final EmployeeService employeeService;

    public LeaveRequestService(
            @Lazy LeaveRequestRepository leaveRequestRepository,
            @Lazy EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository,
            RequestStatusRepository requestStatusRepository,
            RequestStatusMapper requestStatusMapper,
            @Lazy EmployeeService employeeService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.requestStatusMapper = requestStatusMapper;
        this.employeeService = employeeService;
    }

    private static final String STATUS_MENUNGGU = "MENUNGGU_PERSETUJUAN";
    private static final String STATUS_DISETUJUI = "DISETUJUI";
    private static final String STATUS_DITOLAK = "DITOLAK";

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(UUID id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    public List<LeaveRequest> getLeaveRequestsByKaryawanId(UUID karyawanId) {
        return leaveRequestRepository.findByKaryawanId(karyawanId);
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatusNamaStatus(STATUS_MENUNGGU);
    }

    @Transactional
    public LeaveRequest createLeaveRequest(UUID karyawanId, UUID jenisCutiId, LocalDate tglMulai, LocalDate tglSelesai, String alasan) {
        // Validate dates
        if (tglMulai.isAfter(tglSelesai)) {
            throw new RuntimeException("Tanggal mulai tidak boleh setelah tanggal selesai");
        }

        // Get employee
        Employee karyawan = employeeRepository.findById(karyawanId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + karyawanId));

        // Get leave type
        LeaveType jenisCuti = leaveTypeRepository.findById(jenisCutiId)
                .orElseThrow(() -> new RuntimeException("Leave type not found with id: " + jenisCutiId));

        // Calculate days
        long jumlahHari = ChronoUnit.DAYS.between(tglMulai, tglSelesai) + 1;

        // Validate leave balance
        int sisaCuti = karyawan.getSisaCuti() != null ? karyawan.getSisaCuti() : 0;
        if (jumlahHari > sisaCuti) {
            throw new RuntimeException("Sisa cuti tidak mencukupi. Tersedia: " + sisaCuti + " hari, Diminta: " + jumlahHari + " hari");
        }

        // Get pending status
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_MENUNGGU)
                .orElseThrow(() -> new RuntimeException("Status MENUNGGU_PERSETUJUAN tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        // Create leave request
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .karyawan(karyawan)
                .jenisCuti(jenisCuti)
                .status(status)
                .tglMulai(tglMulai)
                .tglSelesai(tglSelesai)
                .alasan(alasan)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest approveLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);

        // Validate current status
        if (!STATUS_MENUNGGU.equals(leaveRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Pengajuan cuti sudah diproses sebelumnya");
        }

        // Calculate days
        long jumlahHari = ChronoUnit.DAYS.between(leaveRequest.getTglMulai(), leaveRequest.getTglSelesai()) + 1;

        // Deduct leave balance
        employeeService.deductLeaveBalance(leaveRequest.getKaryawan().getId(), (int) jumlahHari);

        // Update status to approved
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_DISETUJUI)
                .orElseThrow(() -> new RuntimeException("Status DISETUJUI tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        leaveRequest.setStatus(status);
        leaveRequest.setUpdatedAt(Instant.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest rejectLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);

        // Validate current status
        if (!STATUS_MENUNGGU.equals(leaveRequest.getStatus().getNamaStatus())) {
            throw new RuntimeException("Pengajuan cuti sudah diproses sebelumnya");
        }

        // Update status to rejected
        RequestStatusEntity statusEntity = requestStatusRepository.findByNamaStatus(STATUS_DITOLAK)
                .orElseThrow(() -> new RuntimeException("Status DITOLAK tidak ditemukan"));
        RequestStatus status = requestStatusMapper.toDomain(statusEntity);

        leaveRequest.setStatus(status);
        leaveRequest.setUpdatedAt(Instant.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    public long calculateLeaveDays(LocalDate tglMulai, LocalDate tglSelesai) {
        return ChronoUnit.DAYS.between(tglMulai, tglSelesai) + 1;
    }
}
