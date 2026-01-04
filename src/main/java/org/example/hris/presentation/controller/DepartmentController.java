package org.example.hris.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hris.application.dto.department.DepartmentCreateRequest;
import org.example.hris.application.dto.department.DepartmentResponse;
import org.example.hris.application.dto.department.DepartmentUpdateRequest;
import org.example.hris.application.service.DepartmentService;
import org.example.hris.domain.model.Department;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentCreateRequest request) {
        Department department = Department.builder()
                .id(UUID.randomUUID())
                .namaDepartement(request.getNamaDepartment())
                .build();

        Department created = departmentService.createDepartment(department);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> findAll() {
        List<DepartmentResponse> responses = departmentService.getAllDepartments()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> findById(@PathVariable UUID id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(toResponse(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentUpdateRequest request
    ) {
        Department updated = Department.builder()
                .namaDepartement(request.getNamaDepartment())
                .build();

        Department saved = departmentService.updateDepartment(id, updated);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .namaDepartment(department.getNamaDepartement())
                .build();
    }
}
