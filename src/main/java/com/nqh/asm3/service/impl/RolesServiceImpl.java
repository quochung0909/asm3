package com.nqh.asm3.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.Roles;
import com.nqh.asm3.repository.RolesRepository;
import com.nqh.asm3.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Roles findById(int id) {
        return rolesRepository.findById(id);
    }
    
}
