package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.LeaveRequest;
import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {EmployeeMapper.class, LeaveTypeMapper.class, RequestStatusMapper.class, DateTimeMapper.class}
)
public interface LeaveRequestMapper {

    @Mapping(target = "karyawan", source = "karyawan")
    @Mapping(target = "jenisCuti", source = "jenisCuti")
    @Mapping(target = "status", source = "status")
    LeaveRequest toDomain(LeaveRequestEntity entity);

    @InheritInverseConfiguration
    LeaveRequestEntity toEntity(LeaveRequest domain);
}
