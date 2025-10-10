package org.example.hris.application.dto.position;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PositionUpdateRequest {
    @NotBlank(message = "Nama jabatan / posisi tidak boleh kosong")
    private String namaJabatan;
}
