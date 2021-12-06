package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class PatientRegisterDTO {

    @NotNull
    @Length(min = 7)
    private String fullName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6)
    private String password;
    @NotNull
    private String cmnd;
    @NotNull
    private int sdt;

    @NotNull
    private String dateofbirth;

    @NotNull
    private boolean gender;

    private String image_url;

    private String address;
}
