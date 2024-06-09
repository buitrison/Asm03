package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "id")
    private User doctor;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "maxBooking")
    private String maxBooking;

    @Column(name = "sumBooking")
    private String sumBooking;

}
