package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.employee.model.Employee;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {PositionMapper.class, DepartmentMapper.class, DateTimeMapper.class}
)
public interface EmployeeMapper {

    @Mapping(target = "jabatan", source = "jabatan")
    @Mapping(target = "departemen", source = "departemen")
    Employee toDomain(EmployeeEntity entity);

    @InheritInverseConfiguration
    EmployeeEntity toEntity(Employee domain);
}
