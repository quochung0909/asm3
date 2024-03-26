package com.nqh.asm3.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.DoctorUsers;
import com.nqh.asm3.pojo.Response;
import com.nqh.asm3.pojo.Roles;
import com.nqh.asm3.pojo.Schedules;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.service.BookingsService;
import com.nqh.asm3.service.ClinicsService;
import com.nqh.asm3.service.DoctorUsersService;
import com.nqh.asm3.service.SchedulesService;
import com.nqh.asm3.service.SpecializationsService;
import com.nqh.asm3.service.UsersService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UsersService usersService;
    @Autowired 
    private DoctorUsersService doctorUsersService;
    @Autowired
    private ClinicsService clinicsService;
    @Autowired
    private SpecializationsService specializationsService;
    @Autowired
    private SchedulesService schedulesService;
    @Autowired
    private BookingsService bookingsService;

    // Chức năng khoá user
    @PostMapping("/lockUser/{id}")
    public ResponseEntity<?> lockUser(@PathVariable int id, @RequestBody Users userUpdate) {
        try {
            Users user = usersService.getUser(id);
            if (user == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            user.setActive(false);
            user.setDescription(userUpdate.getDescription());
            user.setUpdatedAt(LocalDateTime.now());
            usersService.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng mở khoá user
    @PostMapping("/unlockUser/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable int id) {
        try {
            Users user = usersService.getUser(id);
            if (user == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("User not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            usersService.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng thêm bác sĩ
    @PostMapping("/addDoctor")
    public ResponseEntity<?> addDoctor(@RequestBody DoctorUsers doctor) {
        try {

            // Kiểm tra xem doctorId có tồn tại không
            if (usersService.getUser(doctor.getDoctorId().getId()) == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("User not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Kiểm tra xem clinicId có tồn tại không
            if (clinicsService.findById(doctor.getClinicId().getId()) == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Clinic not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Kiểm tra xem specializationId có tồn tại không
            if (specializationsService.findById(doctor.getSpecializationId().getId()) == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Specialization not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Kiểm tra xem doctor đã tồn tại chưa
            if (doctorUsersService.findByDoctorId(doctor.getDoctorId()) != null) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Doctor already exists");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Roles role = new Roles();
            role.setId(4);

            Users user = usersService.getUser(doctor.getDoctorId().getId());
            user.setRoleId(role);
            usersService.addUser(user);

            doctor.setCreatedAt(LocalDateTime.now());
            doctorUsersService.save(doctor);
            return ResponseEntity.status(HttpStatus.OK).body("Doctor added successfully");
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
    }

    // Chức năng thêm lịch hẹn
    @PostMapping("/addSchedule")
    public ResponseEntity<?> addSchedule(@RequestBody Schedules schedules) {
        try {
            schedules.setIsActive(true);
            schedules.setStatus("active");
            schedules.setCreatedAt(LocalDateTime.now());
            schedulesService.save(schedules);
            return ResponseEntity.ok("Add schedule success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    // Chức năng xem lịch hẹn của bác sĩ
    @GetMapping("/getScheduleDoctor/{id}")
    public ResponseEntity<?> getScheduleDoctor(@PathVariable int id) {
        try {
            DoctorUsers doctor = doctorUsersService.findById(id);
            if (doctor == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Doctor not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            List<Bookings> bookings = bookingsService.getBookingsByDoctor(doctor.getDoctorId().getEmail());

            if(bookings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bác sĩ chưa có lịch hẹn");
            }
            
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }


}
