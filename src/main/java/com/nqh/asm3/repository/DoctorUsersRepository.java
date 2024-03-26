package com.nqh.asm3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Users;

@Repository
public interface DoctorUsersRepository extends JpaRepository<DoctorUsers, Integer>{
    Optional<DoctorUsers> findByDoctorId(Users users);
}
