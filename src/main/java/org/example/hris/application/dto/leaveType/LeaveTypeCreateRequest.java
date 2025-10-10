package org.example.hris.application.dto.leaveType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeaveTypeCreateRequest {

    @NotBlank(message = "Nama jenis cuti wajib diisi")
    private String namaJenis;
}
