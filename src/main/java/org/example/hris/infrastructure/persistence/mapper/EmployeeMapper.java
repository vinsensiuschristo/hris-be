package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Employee;
import org.example.hris.infrastructure.persistence.entity.EmployeeEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "jabatan.id", source = "jabatan.id")
    @Mapping(target = "jabatan.namaJabatan", source = "jabatan.namaJabatan")
    @Mapping(target = "departemen.id", source = "departemen.id")
    @Mapping(target = "departemen.namaDepartement", source = "departemen.namaDepartemen")
    Employee toDomain(EmployeeEntity entity);

    @Mapping(target = "jabatan", ignore = true)
    @Mapping(target = "departemen", ignore = true)
    EmployeeEntity toEntity(Employee domain);
}
