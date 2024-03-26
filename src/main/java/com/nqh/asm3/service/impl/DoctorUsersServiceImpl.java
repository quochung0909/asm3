package com.nqh.asm3.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.repository.DoctorUsersRepository;
import com.nqh.asm3.service.DoctorUsersService;

@Service
public class DoctorUsersServiceImpl implements DoctorUsersService{

    @Autowired
    private DoctorUsersRepository doctorUsersRepository;

    @Override
    public void save(DoctorUsers doctorUsers) {
        doctorUsersRepository.save(doctorUsers);
    }

    @Override
    public DoctorUsers findByDoctorId(Users users) {
        if (doctorUsersRepository.findByDoctorId(users).isPresent()) {
            return doctorUsersRepository.findByDoctorId(users).get();
        }
        return null;
    }

    @Override
    public DoctorUsers findById(int id) {
        if (doctorUsersRepository.findById(id).isPresent()) {
            return doctorUsersRepository.findById(id).get();
        }
        return null;
    }
    
}
