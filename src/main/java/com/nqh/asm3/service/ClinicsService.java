package com.nqh.asm3.service;

import java.util.List;

import com.nqh.asm3.pojo.Clinics;

public interface ClinicsService {
    Clinics findById(int id);

    Clinics topClinics();

    List<Clinics> findByAddress(String kw);

    List<Clinics> findByName(String kw);
}
