package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BacSiXinNghiDTO {
    @NotNull
    private String date;
    @NotNull
    private String description;
}
