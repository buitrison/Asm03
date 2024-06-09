package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "statusId", referencedColumnName = "id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "specializationId", referencedColumnName = "id")
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "clinicId", referencedColumnName = "id")
    private Clinic clinic;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "dateBooking")
    private String dateBooking;

    @Column(name = "timeBooking")
    private String timeBooking;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "year")
    private String year;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "reason")
    private String reason;

    @Column(name = "price")
    private String price;

    @OneToOne
    @JoinColumn(name = "recordId", referencedColumnName = "id")
    private MedicalRecord medicalRecord;

    @Column(name = "isSentForms")
    private int isSentForms;

    @Column(name = "isTakeCare")
    private int isTakeCare;
}
