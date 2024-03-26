package com.nqh.asm3.service;

import java.util.List;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Schedules;

public interface SchedulesService {
    void save(Schedules schedules);
    Schedules findById(int id);

    List<Schedules> findByPrice(String price);

    List<Schedules> findByDoctor(DoctorUsers doctorUsers);
}
