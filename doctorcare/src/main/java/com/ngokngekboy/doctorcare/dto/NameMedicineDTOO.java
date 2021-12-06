package com.ngokngekboy.doctorcare.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NameMedicineDTOO {
    @NotNull
    private String name;
}
