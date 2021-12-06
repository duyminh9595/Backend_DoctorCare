package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ThemThuocMoiDTO {
    @NotNull
    private String name;
    @NotNull
    private float price;
}
