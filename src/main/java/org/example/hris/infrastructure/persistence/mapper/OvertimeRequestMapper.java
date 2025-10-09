package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.overtime.model.OvertimeRequest;
import org.example.hris.infrastructure.persistence.entity.OvertimeRequestEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, RequestStatusMapper.class, DateTimeMapper.class})
public interface OvertimeRequestMapper {
    @Mapping(target = "karyawan", source = "karyawan")
    @Mapping(target = "status", source = "status")
    OvertimeRequest toDomain(OvertimeRequestEntity entity);

    @Mapping(target = "karyawan", source = "karyawan")
    @Mapping(target = "status", source = "status")
    OvertimeRequestEntity toEntity(OvertimeRequest domain);
}