package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class SelfRegisterDTO {
    @NotNull
    @Length(min = 7)
    private String fullName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6)
    private String password;
}
