package com.nqh.asm3.pojo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name="doctorId")
    private Users doctorId;
    @ManyToOne(targetEntity = Clinics.class)
    @JoinColumn(name="clinicId")
    private Clinics clinicId;
    @ManyToOne(targetEntity = Specializations.class)
    @JoinColumn(name="specializationId")
    private Specializations specializationId;

    @OneToMany(targetEntity = Schedules.class, mappedBy = "doctorId")
    private List<Schedules> schedules;
}
