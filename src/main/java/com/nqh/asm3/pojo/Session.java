package com.nqh.asm3.pojo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Token là class dùng để lưu thông tin token
 * 
 * id: id của token
 * token: token được sinh ra từ JWT
 * message: thông báo
 * timestamp: thời gian tạo token
 * expiration: thời gian hết hạn của token
 * userInfo: thông tin user
 * 
 * Class này sẽ được sử dụng trong JwtApplication
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime expires;
    private String data;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name="userId")
    private Users userId;
}
