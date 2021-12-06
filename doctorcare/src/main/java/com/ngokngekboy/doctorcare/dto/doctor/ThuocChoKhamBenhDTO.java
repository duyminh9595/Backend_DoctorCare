package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ThuocChoKhamBenhDTO {
    @NotNull
    private Long thuocid;
    @NotNull
    private Long quantity;
    @NotNull
    private String name;
    @NotNull
    private float price;
}
