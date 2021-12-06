package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "admin")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email",nullable = false)
    @Email
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(name = "status")
    private boolean status;

    @Column(name = "image_url")
    private String image_url;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "admin")
    private List<Thuoc> thuocUpdatePrice ;


}
