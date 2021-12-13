package com.ngokngekboy.doctorcare.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class TenKhoaDTO {
    @NotNull
    @Length(min = 1)
    private String name;
}
