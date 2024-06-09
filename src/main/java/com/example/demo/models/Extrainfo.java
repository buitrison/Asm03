package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "extrainfos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Extrainfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @Column(name = "historyBreath")
    private String historyBreath;

    @ManyToOne
    @JoinColumn(name = "placeId")
    private Place place;

    @Column(name = "oldForms")
    private String oldForms;

    @Column(name = "sendForms")
    private String sendForms;

    @Column(name = "moreInfo")
    private String moreInfo;


}
