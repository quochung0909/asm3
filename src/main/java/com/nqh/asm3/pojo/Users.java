package com.nqh.asm3.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private String gender;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToOne(targetEntity = Session.class, mappedBy = "userId")
    @JsonIgnore
    private Session session;

    @OneToMany(targetEntity = DoctorUsers.class, mappedBy = "doctorId")
    @JsonIgnore
    private List<DoctorUsers> doctorUserList;

    @ManyToOne(targetEntity = Roles.class)
    @JoinColumn(name="roleId")
    @NotNull(message = "Role is required")
    private Roles roleId;

}
