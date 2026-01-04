package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.LeaveRequest;
import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class}, imports = {java.util.UUID.class})
public interface LeaveRequestMapper {

    @Mapping(target = "karyawan.id", source = "karyawan.id")
    @Mapping(target = "karyawan.nama", source = "karyawan.nama")
    @Mapping(target = "karyawan.nik", source = "karyawan.nik")
    @Mapping(target = "karyawan.email", source = "karyawan.email")
    @Mapping(target = "karyawan.sisaCuti", source = "karyawan.sisaCuti")
    @Mapping(target = "karyawan.jabatan.id", source = "karyawan.jabatan.id")
    @Mapping(target = "karyawan.jabatan.namaJabatan", source = "karyawan.jabatan.namaJabatan")
    @Mapping(target = "karyawan.departemen.id", source = "karyawan.departemen.id")
    @Mapping(target = "karyawan.departemen.namaDepartement", source = "karyawan.departemen.namaDepartemen")
    @Mapping(target = "jenisCuti.id", source = "jenisCuti.id")
    @Mapping(target = "jenisCuti.namaJenis", source = "jenisCuti.namaJenis")
    @Mapping(target = "status.id", source = "status.id")
    @Mapping(target = "status.namaStatus", source = "status.namaStatus")
    LeaveRequest toDomain(LeaveRequestEntity entity);

    @Mapping(target = "id", expression = "java(domain.getId() != null ? domain.getId() : UUID.randomUUID())")
    @Mapping(target = "karyawan", ignore = true)
    @Mapping(target = "jenisCuti", ignore = true)
    @Mapping(target = "status", ignore = true)
    LeaveRequestEntity toEntity(LeaveRequest domain);
}

