package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "image_url",nullable = false)
    private String image_url;

    @Column(name = "date_created")
    private Date date_created;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "enable_status")
    private boolean enable_status;

    @Column(name = "token_confirm_email")
    private String token_confirm_email;

    @Column(name = "cmnd",nullable = false)
    private String cmnd;

    @Column(name = "sdt",nullable = false)
    private int sdt;

    @Column(name = "gender",nullable = false)
    private boolean gender;

    @Column(name = "bangcap",nullable = false)
    private String bangcap;


    @ManyToOne
    @JoinColumn(nullable = false,name = "khoa_id")
    private Khoa khoa;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "doctor")
    private List<HoSoBenhAn> hoSoBenhAnSet ;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "doctor")
    private List<LichHenKham>lichHenKhams  ;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "doctor")
    private List<LichNghiBacSi> lichNghiBacSiSet ;

    public void ThemLichHenKham(LichHenKham lichHenKham)
    {
        if(lichHenKhams==null)
        {
            lichHenKhams=new ArrayList<>();

        }
        lichHenKhams.add(lichHenKham);
        lichHenKham.setDoctor(this);
    }
    public void ThemLichNghi(LichNghiBacSi lichNghiBacSi)
    {
        if(lichNghiBacSi!=null)
        {
            if(lichNghiBacSiSet==null)
                lichNghiBacSiSet=new ArrayList<>();
            lichNghiBacSiSet.add(lichNghiBacSi);
            lichNghiBacSi.setDoctor(this);
        }
    }

}
