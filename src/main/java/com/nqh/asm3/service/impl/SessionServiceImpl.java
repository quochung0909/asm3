package com.nqh.asm3.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.Session;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.repository.SessionRepository;
import com.nqh.asm3.service.SessionService;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public Session findByUserId(Users byEmail) {
        return sessionRepository.findByUserId(byEmail);
    }
    
}
