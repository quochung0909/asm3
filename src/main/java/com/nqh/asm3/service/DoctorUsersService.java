package com.nqh.asm3.service;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Users;

public interface DoctorUsersService {
    void save(DoctorUsers doctorUsers);
    DoctorUsers findByDoctorId(Users users);
    DoctorUsers findById(int id);
}
