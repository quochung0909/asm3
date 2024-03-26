package com.nqh.asm3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Schedules;
import com.nqh.asm3.repository.SchedulesRepository;
import com.nqh.asm3.service.SchedulesService;

@Service
public class SchedulesServiceImpl implements SchedulesService{
    @Autowired
    private SchedulesRepository schedulesRepository;

    @Override
    public void save(Schedules schedules) {
        this.schedulesRepository.save(schedules);
    }

    @Override
    public Schedules findById(int id) {
        if(this.schedulesRepository.findById(id).isPresent()) {
            return this.schedulesRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public List<Schedules> findByPrice(String price) {
        return this.schedulesRepository.findByPriceContaining(price);
    }

    @Override
    public List<Schedules> findByDoctor(DoctorUsers doctorUsers) {
        return this.schedulesRepository.findByDoctorId(doctorUsers);
    }
    
}
