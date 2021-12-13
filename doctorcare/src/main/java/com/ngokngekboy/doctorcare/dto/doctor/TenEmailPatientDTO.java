package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class TenEmailPatientDTO {
    @NotNull
    @Length(min = 1)
    private String name_or_email;
}
