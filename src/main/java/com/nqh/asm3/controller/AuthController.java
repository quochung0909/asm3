package com.nqh.asm3.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nqh.asm3.component.JwtService;
import com.nqh.asm3.pojo.AuthRequest;
import com.nqh.asm3.pojo.ForgotPasswordRequest;
import com.nqh.asm3.pojo.Response;
import com.nqh.asm3.pojo.Session;
import com.nqh.asm3.pojo.Users;
import com.nqh.asm3.service.EmailService;
import com.nqh.asm3.service.SessionService;
import com.nqh.asm3.service.UsersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private EmailService mailService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring Security tutorials !!";
    }

    // Chức năng thêm user
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@Valid @RequestBody Users userInfo, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            // Kiểm tra xem username đã tồn tại chưa
            if (usersService.findByEmail(userInfo.getEmail()) != null) {
                // Nếu username đã tồn tại, trả về thông báo lỗi
                Response res = new Response();
                res.setMessage("Username is already taken");
                res.setStatus(HttpStatus.BAD_REQUEST);
                res.setTimestamp(LocalDateTime.now());

                // Trả về response thông báo lỗi
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);

            } else {
                // Nếu username chưa tồn tại, thêm mới user
                usersService.addUser(userInfo);

                // Trả về thông báo thành công
                Response res = new Response();
                res.setMessage("User added successfully");
                res.setStatus(HttpStatus.CREATED);
                res.setTimestamp(LocalDateTime.now());

                // Trả về response thông báo thành công
                return new ResponseEntity<>(res, HttpStatus.CREATED);
            }

        } catch (Exception e) {

            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            Response res = new Response();
            res.setMessage("Internal server error");
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setTimestamp(LocalDateTime.now());

            // Trả về response thông báo lỗi
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Chức năng đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> addUser(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            // Kiểm tra xem user này đã có token chưa
            Session session = sessionService.findByUserId(usersService.findByEmail(authRequest.getEmail()));

            // Nếu user đã có token, kiểm tra xem token còn hạn không
            if (session != null) {
                // Nếu token còn hạn, trả về token
                if (session.getExpires().isAfter(LocalDateTime.now())) {
                    Map<String, String> map = new HashMap<>();
                    map.put("token", session.getData());
                    return ResponseEntity.ok(map);
                } else {
                    // Nếu token hết hạn, tạo mới token và lưu vào lại database
                    String token = jwtService.generateToken(authRequest.getEmail());
                    Date expiredTime = jwtService.extractExpiration(token);
                    LocalDateTime expiredTimeLocal = expiredTime.toInstant().atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime();
                    session.setData(token);
                    session.setExpires(expiredTimeLocal);
                    session.setUpdatedAt(LocalDateTime.now());
                    sessionService.save(session);

                    Map<String, String> map = new HashMap<>();
                    map.put("token", session.getData());
                    return ResponseEntity.ok(map);
                }
            } else {
                // Nếu user chưa có token, tạo mới token và lưu vào database
                String token = jwtService.generateToken(authRequest.getEmail());
                Date expiredTime = jwtService.extractExpiration(token);
                LocalDateTime expiredTimeLocal = expiredTime.toInstant().atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();
                session = new Session();
                session.setData(token);
                session.setExpires(expiredTimeLocal);
                session.setCreatedAt(LocalDateTime.now());
                session.setUpdatedAt(LocalDateTime.now());
                session.setUserId(usersService.findByEmail(authRequest.getEmail()));
                sessionService.save(session);

                Map<String, String> map = new HashMap<>();
                map.put("token", session.getData());
                return ResponseEntity.ok(map);
            }
        } catch (AuthenticationException e) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

    // Chức năng quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Users user = usersService.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng với địa chỉ email này.");
        } else {
            // Kiểm tra xem user này đã có token chưa
            Session session = sessionService.findByUserId(user);
            if (session != null) {
                // Nếu user đã có token, kiểm tra xem token còn hạn không
                if (session.getExpires().isAfter(LocalDateTime.now())) {
                    // Nếu token còn hạn, gửi email chứa token cũ
                    mailService.sendSimpleMessage(request.getEmail(), "Password Reset Request",
                            "Please click on the following link to reset your password: " + session.getData());
                    return ResponseEntity.ok().body("Token đã được gửi đến địa chỉ email của bạn.");
                } else {
                    // Nếu token hết hạn, tạo mới token và lưu vào lại database
                    String token = jwtService.generateToken(request.getEmail());
                    Date expiredTime = jwtService.extractExpiration(token);
                    LocalDateTime expiredTimeLocal = expiredTime.toInstant().atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime();
                    session.setData(token);
                    session.setExpires(expiredTimeLocal);
                    session.setUpdatedAt(LocalDateTime.now());
                    sessionService.save(session);

                    // Gửi email chứa token mới
                    mailService.sendSimpleMessage(request.getEmail(), "Password Reset Request",
                            "Please click on the following link to reset your password: " + token);

                    return ResponseEntity.ok().body("Token mới đã được gửi đến địa chỉ email của bạn.");
                }
            } else {
                // Nếu user chưa có token, tạo mới token và lưu vào database
                String token = jwtService.generateToken(request.getEmail());
                Date expiredTime = jwtService.extractExpiration(token);
                LocalDateTime expiredTimeLocal = expiredTime.toInstant().atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();
                session = new Session();
                session.setData(token);
                session.setExpires(expiredTimeLocal);
                session.setCreatedAt(LocalDateTime.now());
                session.setUpdatedAt(LocalDateTime.now());
                session.setUserId(user);
                sessionService.save(session);

                // Gửi email chứa token mới
                mailService.sendSimpleMessage(request.getEmail(), "Password Reset Request",
                        "Please click on the following link to reset your password: " + token);

                return ResponseEntity.ok().body("Token mới đã được gửi đến địa chỉ email của bạn.");
            }

        }
    }

    // Chức năng reset mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordRequest request) {
        // Kiểm tra xem token có hợp lệ không
        if (jwtService.validateToken(request.getToken(), usersService.loadUserByUsername(request.getEmail()))) {
            // Nếu token hợp lệ, cập nhật mật khẩu mới
            Users user = usersService.findByEmail(request.getEmail());
            user.setPassword(request.getNewPassword());
            usersService.addUser(user);

            // Trả về thông báo thành công
            Response res = new Response();
            res.setMessage("Password reset successfully");
            res.setStatus(HttpStatus.OK);
            res.setTimestamp(LocalDateTime.now());

            // Trả về response thông báo thành công
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            // Nếu token không hợp lệ, trả về thông báo lỗi
            Response res = new Response();
            res.setMessage("Invalid token");
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.setTimestamp(LocalDateTime.now());

            // Trả về response thông báo lỗi
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

}
