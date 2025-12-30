package org.example.hris.application.service;

import lombok.RequiredArgsConstructor;
import org.example.hris.domain.model.Department;
import org.example.hris.domain.model.Employee;
import org.example.hris.domain.model.Position;
import org.example.hris.domain.repository.DepartmentRepository;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.domain.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public Employee getEmployeeByNik(String nik) {
        return employeeRepository.findByNik(nik)
                .orElseThrow(() -> new RuntimeException("Employee not found with NIK: " + nik));
    }

    @Transactional
    public Employee createEmployee(String nama, String nik, String email, UUID jabatanId, UUID departemenId, Integer sisaCuti) {
        // Check NIK uniqueness
        if (employeeRepository.findByNik(nik).isPresent()) {
            throw new RuntimeException("NIK already exists: " + nik);
        }

        // Check email uniqueness
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists: " + email);
        }

        Position jabatan = null;
        if (jabatanId != null) {
            jabatan = positionRepository.findById(jabatanId)
                    .orElseThrow(() -> new RuntimeException("Position not found with id: " + jabatanId));
        }

        Department departemen = null;
        if (departemenId != null) {
            departemen = departmentRepository.findById(departemenId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departemenId));
        }

        Employee employee = Employee.builder()
                .nama(nama)
                .nik(nik)
                .email(email)
                .jabatan(jabatan)
                .departemen(departemen)
                .sisaCuti(sisaCuti != null ? sisaCuti : 12) // Default 12 days
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(UUID id, String nama, String email, UUID jabatanId, UUID departemenId, Integer sisaCuti) {
        Employee existingEmployee = getEmployeeById(id);

        // Check email uniqueness if changed
        if (email != null && !email.equals(existingEmployee.getEmail())) {
            if (employeeRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already exists: " + email);
            }
            existingEmployee.setEmail(email);
        }

        if (nama != null) {
            existingEmployee.setNama(nama);
        }

        if (jabatanId != null) {
            Position jabatan = positionRepository.findById(jabatanId)
                    .orElseThrow(() -> new RuntimeException("Position not found with id: " + jabatanId));
            existingEmployee.setJabatan(jabatan);
        }

        if (departemenId != null) {
            Department departemen = departmentRepository.findById(departemenId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departemenId));
            existingEmployee.setDepartemen(departemen);
        }

        if (sisaCuti != null) {
            existingEmployee.setSisaCuti(sisaCuti);
        }

        existingEmployee.setUpdatedAt(Instant.now());

        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        // Verify employee exists
        getEmployeeById(id);
        employeeRepository.deleteById(id);
    }

    @Transactional
    public Employee deductLeaveBalance(UUID id, int days) {
        Employee employee = getEmployeeById(id);
        int currentBalance = employee.getSisaCuti() != null ? employee.getSisaCuti() : 0;
        
        if (currentBalance < days) {
            throw new RuntimeException("Insufficient leave balance. Available: " + currentBalance + ", Requested: " + days);
        }
        
        employee.setSisaCuti(currentBalance - days);
        employee.setUpdatedAt(Instant.now());
        
        return employeeRepository.save(employee);
    }
}
