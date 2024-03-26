package com.nqh.asm3.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.Clinics;
import com.nqh.asm3.pojo.Response;
import com.nqh.asm3.pojo.Schedules;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.service.BookingsService;
import com.nqh.asm3.service.ClinicsService;
import com.nqh.asm3.service.SchedulesService;
import com.nqh.asm3.service.SpecializationsService;
import com.nqh.asm3.service.UsersService;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private BookingsService bookingsService;
    @Autowired
    private SchedulesService schedulesService;
    @Autowired
    private SpecializationsService specializationsService;
    @Autowired
    private ClinicsService clinicsService;

    // Chức năng hiển thị thông tin user đã đăng nhập
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getAllUsers(@PathVariable int id) {

        try {
            Users user = usersService.getUser(id);

            if (user == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Chức năng cập nhật thông tin user đã đăng nhập
    //
    // format Api
    // {
    // "email": "johndoe8@gmail.com",
    // "password" : "123456",
    // "roleId" : {
    //     "id" : 1
    //  }
    // }
    @PostMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody Users userUpdate, @PathVariable int id) {
        try {
            Users user = usersService.getUser(id);
            if (user == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            user = userUpdate;
            user.setUpdatedAt(LocalDateTime.now());
            user.setId(id);
            usersService.addUser(user);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng đặt lịch hẹn
    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestBody Bookings booking) {
        try {
            if(schedulesService.findById(booking.getScheduleId().getId()) == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Schedule is not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            bookingsService.save(booking);
            return ResponseEntity.status(HttpStatus.OK).body("Booking success");
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng hiển thị chuyên khoa được yêu thích nhất
    @GetMapping("/topSpecializations")
    public ResponseEntity<?> topSpecializations() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(specializationsService.topSpecializations());
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng hiển thị phòng khám được yêu thích nhất
    @GetMapping("/topClinics")
    public ResponseEntity<?> topClinics() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clinicsService.topClinics());
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }   

    // Chức năng search theo chuyên khoa
    @GetMapping("/searchSp")
    public ResponseEntity<?> searchSp(@RequestParam String kw) {
        System.out.println(kw);
        try {
            if (specializationsService.findByKw(kw).isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Specialization not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(specializationsService.findByKw(kw));
            }
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng search theo địa chỉ
    @GetMapping("/searchAddress")
    public ResponseEntity<?> search(@RequestParam String kw) {
        System.out.println(kw);
        try {
            if (clinicsService.findByAddress(kw).isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Specialization not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(clinicsService.findByAddress(kw));
            }
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng search theo giá
    @GetMapping("/searchPrice")
    public ResponseEntity<?> searchPrice(@RequestParam String kw) {
        try {
            if (schedulesService.findByPrice(kw).isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Price not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(schedulesService.findByPrice(kw));
            }
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng search theo tên phòng khám
    @GetMapping("/searchClinics")
    public ResponseEntity<?> searchClinics(@RequestParam String kw) {
        try {
            if (clinicsService.findByName(kw).isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Clinics not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(clinicsService.findByName(kw));
            }
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    


}
