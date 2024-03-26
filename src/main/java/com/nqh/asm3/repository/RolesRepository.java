package com.nqh.asm3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{
    Roles findById(int id);
}
