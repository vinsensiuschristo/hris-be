package org.example.hris.application.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CheckOutRequest {

    @NotNull(message = "Karyawan ID wajib diisi")
    private UUID karyawanId;

    private LocalTime jamKeluar; // Optional, defaults to now
}
