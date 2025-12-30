package org.example.hris.application.dto.overtimeRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class OvertimeRequestCreateRequest {

    @NotNull(message = "Karyawan ID wajib diisi")
    private UUID karyawanId;

    @NotNull(message = "Tanggal lembur wajib diisi")
    private LocalDate tglLembur;

    @NotNull(message = "Jam mulai wajib diisi")
    private LocalTime jamMulai;

    @NotNull(message = "Jam selesai wajib diisi")
    private LocalTime jamSelesai;

    private String keterangan;
}
