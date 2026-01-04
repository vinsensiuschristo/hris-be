package org.example.hris.application.service;

import org.example.hris.domain.model.Department;
import org.example.hris.domain.model.Employee;
import org.example.hris.domain.model.Position;
import org.example.hris.domain.repository.DepartmentRepository;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.domain.repository.PositionRepository;
import org.example.hris.infrastructure.persistence.entity.DepartmentEntity;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.entity.PositionEntity;
import org.example.hris.infrastructure.persistence.repository.DepartmentJpaRepository;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.example.hris.infrastructure.persistence.repository.PositionJpaRepository;
import org.example.hris.infrastructure.persistence.mapper.EmployeeMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final PositionJpaRepository positionJpaRepository;
    private final DepartmentJpaRepository departmentJpaRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(
            @Lazy EmployeeRepository employeeRepository,
            PositionRepository positionRepository,
            DepartmentRepository departmentRepository,
            EmployeeJpaRepository employeeJpaRepository,
            PositionJpaRepository positionJpaRepository,
            DepartmentJpaRepository departmentJpaRepository,
            @Lazy EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeJpaRepository = employeeJpaRepository;
        this.positionJpaRepository = positionJpaRepository;
        this.departmentJpaRepository = departmentJpaRepository;
        this.employeeMapper = employeeMapper;
    }

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

        // Create entity directly - let JPA handle ID generation via @GeneratedValue
        EmployeeEntity entity = EmployeeEntity.builder()
                .nama(nama)
                .nik(nik)
                .email(email)
                .sisaCuti(sisaCuti != null ? sisaCuti : 12)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Set relationships via JPA entities
        if (jabatanId != null) {
            PositionEntity jabatan = positionJpaRepository.findById(jabatanId)
                    .orElseThrow(() -> new RuntimeException("Position not found with id: " + jabatanId));
            entity.setJabatan(jabatan);
        }

        if (departemenId != null) {
            DepartmentEntity departemen = departmentJpaRepository.findById(departemenId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departemenId));
            entity.setDepartemen(departemen);
        }

        EmployeeEntity saved = employeeJpaRepository.save(entity);
        return employeeMapper.toDomain(saved);
    }

    @Transactional
    public Employee updateEmployee(UUID id, String nama, String email, UUID jabatanId, UUID departemenId, Integer sisaCuti) {
        EmployeeEntity entity = employeeJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Check email uniqueness if changed
        if (email != null && !email.equals(entity.getEmail())) {
            if (employeeRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already exists: " + email);
            }
            entity.setEmail(email);
        }

        if (nama != null) {
            entity.setNama(nama);
        }

        // Set relationships via JPA entities
        if (jabatanId != null) {
            PositionEntity jabatan = positionJpaRepository.findById(jabatanId)
                    .orElseThrow(() -> new RuntimeException("Position not found with id: " + jabatanId));
            entity.setJabatan(jabatan);
        } else {
            entity.setJabatan(null);
        }

        if (departemenId != null) {
            DepartmentEntity departemen = departmentJpaRepository.findById(departemenId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departemenId));
            entity.setDepartemen(departemen);
        } else {
            entity.setDepartemen(null);
        }

        if (sisaCuti != null) {
            entity.setSisaCuti(sisaCuti);
        }

        entity.setUpdatedAt(LocalDateTime.now());

        EmployeeEntity saved = employeeJpaRepository.save(entity);
        return employeeMapper.toDomain(saved);
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
        
        EmployeeEntity entity = employeeJpaRepository.findById(id).orElseThrow();
        entity.setSisaCuti(currentBalance - days);
        entity.setUpdatedAt(LocalDateTime.now());
        
        return employeeMapper.toDomain(employeeJpaRepository.save(entity));
    }
}
