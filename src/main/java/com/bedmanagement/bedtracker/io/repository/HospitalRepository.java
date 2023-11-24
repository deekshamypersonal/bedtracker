package com.bedmanagement.bedtracker.io.repository;

import com.bedmanagement.bedtracker.io.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends CrudRepository<Hospital,Long> {

    @Query("select hospital from Hospital hospital where hospital.city=city and hospital.noOfBedsAvailable>0")
    List<Hospital> fillAllByCity(@Param("city") String city);


}
