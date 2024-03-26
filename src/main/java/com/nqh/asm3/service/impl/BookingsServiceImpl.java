package com.nqh.asm3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Schedules;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.repository.BookingsRepository;
import com.nqh.asm3.repository.DoctorUsersRepository;
import com.nqh.asm3.repository.SchedulesRepository;
import com.nqh.asm3.repository.UsersRepository;
import com.nqh.asm3.service.BookingsService;

@Service
public class BookingsServiceImpl implements BookingsService {
    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private DoctorUsersRepository doctorUsersRepository;
    @Autowired
    private SchedulesRepository schedulesRepository;

    @Override
    public void save(Bookings bookings) {
        this.bookingsRepository.save(bookings);
    }

    @Override
    public List<Bookings> getBookingsByDoctor(String mailDoctor) {
        Users doctor = usersRepository.findByEmail(mailDoctor).get();
        DoctorUsers doctorUser = doctorUsersRepository.findByDoctorId(doctor).get();
        List<Schedules> schedules = schedulesRepository.findByDoctorId(doctorUser);
        List<Bookings> bookings = new ArrayList<>();
        for (Schedules schedule : schedules) {
            bookings.addAll(bookingsRepository.findByScheduleId(schedule));
        }


        return bookings;
    }

    @Override
    public Bookings findById(int id) {
        if (bookingsRepository.findById(id).isPresent()) {
            return bookingsRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public void update(Bookings bookings) {
        this.bookingsRepository.save(bookings);
    }

    


}
