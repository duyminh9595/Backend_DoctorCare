package com.ngokngekboy.doctorcare.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "lich_hen_kham")
public class LichHenKham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "patient_id")
    private Patient patient;


    @ManyToOne
    @JoinColumn(nullable = false, name = "doctor_id")
    private Doctor doctor;

    @Column(name = "date_created")
    private Date date_created;

//    0 la sang, 1 la toi
//    binh quan 1 ca kham là 15 min, sáng kham từ 8h đến 11h, chiều từ 1h den 4h,
    @Column(name = "buoi")
    private  boolean buoi;


    @Column(name = "stt")
    private int stt;

    @Column(name = "status")
    private boolean status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "user_send_cancel")
    private boolean user_send_cancel;
}
