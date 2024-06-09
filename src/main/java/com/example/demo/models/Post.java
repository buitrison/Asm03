package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;

    @Column(name = "contentHTML")
    private String contentHTML;

    @Column(name = "contentMarkdown")
    private String contentMarkdown;

    @ManyToOne
    @JoinColumn(name = "forDoctorId", referencedColumnName = "id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "forSpecializationId", referencedColumnName = "id")
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "forClinicId", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "writerId", referencedColumnName = "id")
    private User writer;

    @Column(name = "address")
    private String address;

    @Column(name = "fee")
    private Long fee;

    @Column(name = "image")
    private String image;


}
