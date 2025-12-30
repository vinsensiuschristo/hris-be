package org.example.hris.domain.repository;

import org.example.hris.domain.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    Employee save(Employee employee);

    List<Employee> findAll();

    Optional<Employee> findById(UUID id);

    Optional<Employee> findByNik(String nik);

    Optional<Employee> findByEmail(String email);

    void deleteById(UUID id);
}
