package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clinics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "introductionHTML")
    private String introductionHTML;

    @Column(name = "introductionMarkdown")
    private String introductionMarkdown;

    @Column(name = "time")
    private String time;

    @Column(name = "fee")
    private String fee;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;


}
