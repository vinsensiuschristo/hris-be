package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.employee.model.Employee;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "jabatan", source = "jabatan")
    @Mapping(target = "departemen", source = "departemen")
    Employee toDomain(EmployeeEntity entity);

    @Mapping(target = "jabatan", source = "jabatan")
    @Mapping(target = "departemen", source = "departemen")
    EmployeeEntity toEntity(Employee domain);
}