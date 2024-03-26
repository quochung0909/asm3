package com.nqh.asm3.service;

import java.util.List;

import com.nqh.asm3.pojo.Bookings;

public interface BookingsService {
    void save(Bookings bookings);

    List<Bookings> getBookingsByDoctor(String mailDoctor);

    Bookings findById(int id);

    void update(Bookings bookings);
    
}
