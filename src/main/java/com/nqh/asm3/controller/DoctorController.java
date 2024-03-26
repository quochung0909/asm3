package com.nqh.asm3.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nqh.asm3.pojo.Bookings;
import com.nqh.asm3.pojo.Response;
import com.nqh.asm3.service.BookingsService;
import com.nqh.asm3.service.EmailService;

@RestController
@RequestMapping("/doctor")  
public class DoctorController {
    @Autowired
    private BookingsService bookingsService;

    @Autowired
    private EmailService emailService;

    // Chức năng lấy danh sách bệnh nhân
    @GetMapping("/getPatients")
    public ResponseEntity<?> getPatients() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();

            return ResponseEntity.status(HttpStatus.OK).body(bookingsService.getBookingsByDoctor(currentPrincipalName));


        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
    }

    // Chức năng chấp nhận lịch hẹn
    @PostMapping("/acceptBookings/{id}")
    public ResponseEntity<?> updateBookings(@PathVariable int id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();

            List<Bookings> bookings = bookingsService.getBookingsByDoctor(currentPrincipalName);

            Bookings booking = bookingsService.findById(id);

            if (bookings == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Bookings not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            boolean check = false;
            for (Bookings b : bookings) {
                if (b.getId() == booking.getId()) {
                    check = true;
                    break;
                }
            }

            if (check == false) {
                Response response = new Response();
                response.setStatus(HttpStatus.FORBIDDEN);
                response.setMessage("You don't have permission to update this bookings");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            booking.setActive(true);

            bookingsService.update(booking);
            return ResponseEntity.status(HttpStatus.OK).body("Accept bookings successfully");
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng từ chối lịch hẹn
    @PostMapping("/denyBookings/{id}")
    public ResponseEntity<?> denyBookings(@PathVariable int id, @RequestBody Bookings bookingUpdate) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();

            List<Bookings> bookings = bookingsService.getBookingsByDoctor(currentPrincipalName);

            Bookings booking = bookingsService.findById(id);

            if (bookings == null) {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setMessage("Bookings not found");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            boolean check = false;
            for (Bookings b : bookings) {
                if (b.getId() == booking.getId()) {
                    check = true;
                    break;
                }
            }

            if (check == false) {
                Response response = new Response();
                response.setStatus(HttpStatus.FORBIDDEN);
                response.setMessage("You don't have permission to update this bookings");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            booking.setActive(false);
            booking.setDescription(bookingUpdate.getDescription());

            bookingsService.update(booking);
            return ResponseEntity.status(HttpStatus.OK).body("Deny bookings successfully");
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Chức năng gửi mail cho bệnh nhân
    @PostMapping("/sendMailPatient/{id}")
    public ResponseEntity<?> sendMailPatient(@PathVariable int id, @RequestParam("file") MultipartFile file){
        try {
            if (!file.getContentType().equals("application/pdf")) {
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("File must be pdf");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            System.out.println(currentPrincipalName);

            List<Bookings> bookings = bookingsService.getBookingsByDoctor(currentPrincipalName);
            boolean check = false;

            for (Bookings b : bookings) {
                if (b.getId() == id) {
                    check = true;
                    if (b.isActive() == false) {
                        Response response = new Response();
                        response.setStatus(HttpStatus.BAD_REQUEST);
                        response.setMessage("This bookings is not active");
                        response.setTimestamp(LocalDateTime.now());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                }
            }

            Bookings booking = bookingsService.findById(id);
        

            if (check == false) {
                Response response = new Response();
                response.setStatus(HttpStatus.FORBIDDEN);
                response.setMessage("You don't have permission to send mail to this patient");
                response.setTimestamp(LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            
            emailService.sendAttachmentMessage(booking.getEmail(), "Thông tin khám bệnh", "Thông tin sau khi khám", file);
            return ResponseEntity.status(HttpStatus.OK).body("Send mail successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("Internal server error");
            response.setTimestamp(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
