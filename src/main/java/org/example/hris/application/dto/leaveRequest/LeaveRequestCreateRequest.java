package org.example.hris.application.dto.leaveRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeaveRequestCreateRequest {

    @NotNull(message = "Karyawan ID wajib diisi")
    private UUID karyawanId;

    @NotNull(message = "Jenis cuti wajib diisi")
    private UUID jenisCutiId;

    @NotNull(message = "Tanggal mulai wajib diisi")
    private LocalDate tglMulai;

    @NotNull(message = "Tanggal selesai wajib diisi")
    private LocalDate tglSelesai;

    private String alasan;
}
