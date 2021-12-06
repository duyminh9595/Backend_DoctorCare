package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ForgotPasswordDTO {
    @NotNull
    @Email
    private String email;
}
