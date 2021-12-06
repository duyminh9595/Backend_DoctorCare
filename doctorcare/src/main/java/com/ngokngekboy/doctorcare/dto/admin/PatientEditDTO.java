package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PatientEditDTO {
    @NotNull
    private Long id;
    @NotNull
    private String fullName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;


    @NotNull
    private boolean gender;

    @NotNull
    private boolean status;

    private String image_url;

    private String address;
    @NotNull
    private String cmnd;
    @NotNull
    private int sdt;

    private String date_of_birth;
}
