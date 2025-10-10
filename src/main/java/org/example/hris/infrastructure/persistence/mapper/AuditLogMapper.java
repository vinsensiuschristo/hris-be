package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.security.model.AuditLog;
import org.example.hris.infrastructure.persistence.entity.AuditLogEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, DateTimeMapper.class}
)
public interface AuditLogMapper {

    @Mapping(target = "user", source = "user")
    AuditLog toDomain(AuditLogEntity entity);

    @InheritInverseConfiguration
    AuditLogEntity toEntity(AuditLog domain);
}

