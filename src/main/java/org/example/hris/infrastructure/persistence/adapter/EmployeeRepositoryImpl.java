package org.example.hris.infrastructure.persistence.adapter;

import org.example.hris.domain.model.Employee;
import org.example.hris.domain.repository.EmployeeRepository;
import org.example.hris.infrastructure.persistence.mapper.EmployeeMapper;
import org.example.hris.infrastructure.persistence.repository.EmployeeJpaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeRepositoryImpl(
            EmployeeJpaRepository employeeJpaRepository,
            @Lazy EmployeeMapper employeeMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeMapper.toDomain(
                employeeJpaRepository.save(employeeMapper.toEntity(employee))
        );
    }

    @Override
    public List<Employee> findAll() {
        return employeeJpaRepository.findAll()
                .stream()
                .map(employeeMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return employeeJpaRepository.findById(id)
                .map(employeeMapper::toDomain);
    }

    @Override
    public Optional<Employee> findByNik(String nik) {
        return employeeJpaRepository.findByNik(nik)
                .map(employeeMapper::toDomain);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeJpaRepository.findByEmail(email)
                .map(employeeMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        employeeJpaRepository.deleteById(id);
    }
}
