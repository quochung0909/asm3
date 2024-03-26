package com.nqh.asm3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.Clinics;
import com.nqh.asm3.repository.BookingsRepository;
import com.nqh.asm3.repository.ClinicsRepository;
import com.nqh.asm3.service.ClinicsService;

@Service
public class ClinicsServiceImpl implements ClinicsService {

    @Autowired
    private ClinicsRepository clinicsRepository;
    @Autowired
    private BookingsRepository bookingsRepository;

    @Override
    public Clinics findById(int id) {
        if (clinicsRepository.findById(id).isPresent()) {
            return clinicsRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Clinics topClinics() {
        List<Clinics> clinics = clinicsRepository.findAll();
        List<Bookings> bookings = bookingsRepository.findAll();

        // Mảng count chứa số lượng lịch hẹn của từng phòng khám
        int[] count = new int[clinics.size()];

        // Đếm số lượng lịch hẹn của từng phòng khám
        for (int i = 0; i < clinics.size(); i++) {
            for (int j = 0; j < bookings.size(); j++) {
                if (bookings.get(j).getScheduleId().getDoctorId().getClinicId()
                        .getId() == clinics.get(i).getId()) {
                    count[i]++;
                }
            }
        }

        // Tìm phòng khám có số lượng lịch hẹn nhiều nhất, nếu có nhiều phòng khám có số lượng lịch hẹn bằng nhau thì trả về phòng khám đầu tiên trong mảng clinics
        int max = count[0];
        int index = 0;
        for (int i = 1; i < count.length; i++) {
            if (count[i] > max) {
                max = count[i];
                index = i;
            }
        }

        return clinics.get(index);
    }

    @Override
    public List<Clinics> findByAddress(String kw) {
        return clinicsRepository.findByAddressContaining(kw);
    }


    @Override
    public List<Clinics> findByName(String kw) {
        return clinicsRepository.findByNameContaining(kw);
    }
    
}
