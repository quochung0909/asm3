package com.nqh.asm3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.Specializations;
import com.nqh.asm3.repository.BookingsRepository;
import com.nqh.asm3.repository.SpecializationsRepository;
import com.nqh.asm3.service.SpecializationsService;

@Repository
public class SpecializationsServiceImpl implements SpecializationsService {

    @Autowired
    private SpecializationsRepository specializationsRepository;
    @Autowired
    private BookingsRepository bookingsRepository;

    @Override
    public Specializations findById(int id) {
        if (specializationsRepository.findById(id).isPresent()) {
            return specializationsRepository.findById(id).get();
        } 
        return null;
    }

    @Override
    public Specializations topSpecializations() {
        List<Specializations> specializations = specializationsRepository.findAll();
        List<Bookings> bookings = bookingsRepository.findAll();

        // Mảng count chứa số lượng lịch hẹn của từng chuyên khoa
        int[] count = new int[specializations.size()];

        // Đếm số lượng lịch hẹn của từng chuyên khoa
        for (int i = 0; i < specializations.size(); i++) {
            for (int j = 0; j < bookings.size(); j++) {
                if (bookings.get(j).getScheduleId().getDoctorId().getSpecializationId()
                        .getId() == specializations.get(i).getId()) {
                    count[i]++;
                }
            }
        }

        // Tìm chuyên khoa có số lượng lịch hẹn nhiều nhất, nếu có nhiều chuyên khoa có số lượng lịch hẹn bằng nhau thì trả về chuyên khoa đầu tiên trong mảng specializations 
        int max = count[0];
        int index = 0;
        for (int i = 1; i < count.length; i++) {
            if (count[i] > max) {
                max = count[i];
                index = i;
            }
        }
        
        return specializations.get(index);
    }

    @Override
    public List<Specializations> findByKw(String kw) {
        return specializationsRepository.findByNameContaining(kw);
    }
    
}
