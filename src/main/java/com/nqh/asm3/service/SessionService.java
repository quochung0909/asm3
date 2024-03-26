package com.nqh.asm3.service;

import com.nqh.asm3.pojo.Session;
import com.nqh.asm3.pojo.Users;

public interface SessionService {
    void save(Session session);

    Session findByUserId(Users byEmail);
}
