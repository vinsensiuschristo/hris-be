package org.example.hris.application.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OvertimePaymentCreateRequest {

    @NotNull(message = "Overtime request ID wajib diisi")
    private UUID overtimeRequestId;

    private UUID financeId;
}
