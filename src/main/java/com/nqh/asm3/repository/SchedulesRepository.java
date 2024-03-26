package com.nqh.asm3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Schedules;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Integer>{
    List<Schedules> findByPriceContaining(String price);
    List<Schedules> findByDoctorId(DoctorUsers doctorId);
}
