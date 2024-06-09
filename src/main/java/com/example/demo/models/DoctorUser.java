package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "clinicId", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "specializationId", referencedColumnName = "id")
    private Specialization specialization;

    @Column(name = "process")
    private String process;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "achievement")
    private String achievement;

}
