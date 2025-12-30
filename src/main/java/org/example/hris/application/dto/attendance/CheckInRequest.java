package org.example.hris.application.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CheckInRequest {

    @NotNull(message = "Karyawan ID wajib diisi")
    private UUID karyawanId;

    private LocalTime jamMasuk; // Optional, defaults to now

    private String keterangan;
}
