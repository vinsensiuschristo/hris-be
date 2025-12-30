package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Attendance;
import org.example.hris.infrastructure.persistence.entity.AttendanceEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {EmployeeMapper.class, DateTimeMapper.class}
)
public interface AttendanceMapper {

    @Mapping(target = "karyawan", source = "karyawan")
    Attendance toDomain(AttendanceEntity entity);

    @InheritInverseConfiguration
    AttendanceEntity toEntity(Attendance domain);
}
