package com.ngokngekboy.doctorcare.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6)
    private String password;
}
