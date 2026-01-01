package org.example.hris.infrastructure.persistence.repository;

import org.example.hris.infrastructure.persistence.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("leaveRequestJpaRepository")
public interface LeaveRequestJpaRepository extends JpaRepository<LeaveRequestEntity, UUID> {
    List<LeaveRequestEntity> findByKaryawan_Id(UUID karyawanId);
}
