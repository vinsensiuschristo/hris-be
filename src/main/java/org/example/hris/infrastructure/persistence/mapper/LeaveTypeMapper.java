package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.LeaveType;
import org.example.hris.infrastructure.persistence.entity.LeaveTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaveTypeMapper {
    LeaveType toDomain(LeaveTypeEntity entity);

    LeaveTypeEntity toEntity(LeaveType domain);
}