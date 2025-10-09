package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.leave.model.RequestStatus;
import org.example.hris.infrastructure.persistence.entity.RequestStatusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestStatusMapper {
    RequestStatus toDomain(RequestStatusEntity entity);

    RequestStatusEntity toEntity(RequestStatus domain);
}