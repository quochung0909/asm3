package com.nqh.asm3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.Session;
import com.nqh.asm3.pojo.Users;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer>{
    Session findByUserId(Users userId);
}
