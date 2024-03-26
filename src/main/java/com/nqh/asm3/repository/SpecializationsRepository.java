package com.nqh.asm3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.Specializations;

@Repository
public interface SpecializationsRepository extends JpaRepository<Specializations, Integer>{
    List<Specializations> findByNameContaining(String kw);
}
