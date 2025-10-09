package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.overtime.model.OvertimeEvidence;
import org.example.hris.infrastructure.persistence.entity.OvertimeEvidenceEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class})
public interface OvertimeEvidenceMapper {
    @Mapping(target = "overtimeRequest", source = "overtimeRequest")
    OvertimeEvidence toDomain(OvertimeEvidenceEntity entity);

    @Mapping(target = "overtimeRequest", source = "overtimeRequest")
    OvertimeEvidenceEntity toEntity(OvertimeEvidence domain);
}
