package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Department;
import org.example.hris.domain.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(UUID id, Department updatedDepartment) {
        Department existingDepartment = getDepartmentById(id);
        existingDepartment.setNamaDepartement(updatedDepartment.getNamaDepartement());
        return departmentRepository.save(existingDepartment);
    }

    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }
}
