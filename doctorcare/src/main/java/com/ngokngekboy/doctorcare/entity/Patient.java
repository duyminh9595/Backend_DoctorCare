package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "enable_status")
    private boolean enable_status;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "disable_reason")
    private String disable_reason;

    @Column(name = "date_disable")
    private Date date_disable;

    @Column(name = "confirm_email")
    private boolean confirm_email;

    @Column(name = "token_confirm_email")
    private String token_confirm_email;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "address")
    private String address;


    @Column(name = "cmnd",nullable = false)
    private String cmnd;

    @Column(name = "sdt",nullable = false)
    private int sdt;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "patient")
    private Set<HoSoBenhAn> hoSoBenhAnSet ;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "patient")
    private Set<LichHenKham> lichHenKhamSet ;

    public void ThemLichHenKham(LichHenKham lichHenKham)
    {
        if(lichHenKhamSet==null)
        {
            lichHenKhamSet=new HashSet<>();

        }
        lichHenKhamSet.add(lichHenKham);
        lichHenKham.setPatient(this);
    }
}
