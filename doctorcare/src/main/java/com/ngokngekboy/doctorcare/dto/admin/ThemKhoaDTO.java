package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ThemKhoaDTO {
    @NotNull
    private String tenkhoa;
}
