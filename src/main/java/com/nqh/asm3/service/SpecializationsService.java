package com.nqh.asm3.service;

import java.util.List;

import com.nqh.asm3.pojo.Specializations;

public interface SpecializationsService {
    Specializations findById(int id);

    Specializations topSpecializations();

    List<Specializations> findByKw(String kw);
}
