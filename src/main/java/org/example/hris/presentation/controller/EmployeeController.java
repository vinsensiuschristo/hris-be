package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.department.DepartmentResponse;
import org.example.hris.application.dto.employee.EmployeeCreateRequest;
import org.example.hris.application.dto.employee.EmployeeResponse;
import org.example.hris.application.dto.employee.EmployeeUpdateRequest;
import org.example.hris.application.dto.position.PositionResponse;
import org.example.hris.application.service.EmployeeService;
import org.example.hris.domain.model.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        List<EmployeeResponse> responses = employeeService.getAllEmployees()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable UUID id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(toResponse(employee));
    }

    @GetMapping("/nik/{nik}")
    @Operation(summary = "Get employee by NIK")
    public ResponseEntity<EmployeeResponse> findByNik(@PathVariable String nik) {
        Employee employee = employeeService.getEmployeeByNik(nik);
        return ResponseEntity.ok(toResponse(employee));
    }

    @PostMapping
    @Operation(summary = "Create new employee")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest request) {
        Employee created = employeeService.createEmployee(
                request.getNama(),
                request.getNik(),
                request.getEmail(),
                request.getJabatanId(),
                request.getDepartemenId(),
                request.getSisaCuti()
        );
        return ResponseEntity.ok(toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        Employee updated = employeeService.updateEmployee(
                id,
                request.getNama(),
                request.getEmail(),
                request.getJabatanId(),
                request.getDepartemenId(),
                request.getSisaCuti()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    private EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse.EmployeeResponseBuilder builder = EmployeeResponse.builder()
                .id(employee.getId())
                .nama(employee.getNama())
                .nik(employee.getNik())
                .email(employee.getEmail())
                .sisaCuti(employee.getSisaCuti())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt());

        if (employee.getJabatan() != null) {
            builder.jabatan(PositionResponse.builder()
                    .id(employee.getJabatan().getId())
                    .namaJabatan(employee.getJabatan().getNamaJabatan())
                    .build());
        }

        if (employee.getDepartemen() != null) {
            builder.departemen(DepartmentResponse.builder()
                    .id(employee.getDepartemen().getId())
                    .namaDepartment(employee.getDepartemen().getNamaDepartement())
                    .build());
        }

        return builder.build();
    }
}
