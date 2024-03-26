package com.nqh.asm3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.Clinics;

@Repository
public interface ClinicsRepository extends JpaRepository<Clinics, Integer>{
    List<Clinics> findByAddressContaining(String kw);
    List<Clinics> findByNameContaining(String kw);
}
