package org.example.hris.domain.employee.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private UUID id;
    private String nama;
    private String nik;
    private Position jabatan;
    private Department departemen;
    private String email;
    private Integer sisaCuti;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
