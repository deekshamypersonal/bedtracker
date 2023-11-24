package com.bedmanagement.bedtracker.io.repository;

import com.bedmanagement.bedtracker.common.ApprovalStatus;
import com.bedmanagement.bedtracker.io.entity.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient,Long> {

    Patient findByEmail(String email);

    Patient deleteByEmail(String email);

    @Query("select patient from Patient patient where patient.HosptialAllocatedStatus=:status")
    List<Patient> findPending(@Param("status") ApprovalStatus status);

    @Modifying
    @Query("UPDATE Patient patient SET patient.patientArrived = :status WHERE patient.email = :email")
    int updatePatientStatus(@Param("status") String status,@Param("email") String email);


}
