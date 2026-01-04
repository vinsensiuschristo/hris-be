package org.example.hris.infrastructure.persistence.mapper;

import org.example.hris.domain.model.Attendance;
import org.example.hris.infrastructure.persistence.entity.AttendanceEntity;
import org.example.hris.infrastructure.persistence.mapper.common.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class, EmployeeMapper.class})
public interface AttendanceMapper {

    @Mapping(target = "karyawan.id", source = "karyawan.id")
    @Mapping(target = "karyawan.nama", source = "karyawan.nama")
    @Mapping(target = "karyawan.nik", source = "karyawan.nik")
    @Mapping(target = "karyawan.email", source = "karyawan.email")
    @Mapping(target = "karyawan.sisaCuti", source = "karyawan.sisaCuti")
    @Mapping(target = "karyawan.jabatan.id", source = "karyawan.jabatan.id")
    @Mapping(target = "karyawan.jabatan.namaJabatan", source = "karyawan.jabatan.namaJabatan")
    @Mapping(target = "karyawan.departemen.id", source = "karyawan.departemen.id")
    @Mapping(target = "karyawan.departemen.namaDepartement", source = "karyawan.departemen.namaDepartemen")
    Attendance toDomain(AttendanceEntity entity);

    @Mapping(target = "karyawan.id", source = "karyawan.id")
    AttendanceEntity toEntity(Attendance domain);
}

