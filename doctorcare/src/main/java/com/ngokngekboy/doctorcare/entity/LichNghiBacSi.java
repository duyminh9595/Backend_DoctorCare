package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lich_nghi_bac_si")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LichNghiBacSi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne
    @JoinColumn(nullable = false,name = "doctor_id")
    private Doctor doctor;

    @Column(name = "date_off")
    private Date date_off;

    @Column(name = "description")
    private String description;


}
