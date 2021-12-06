package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class KhamBenhDTO {
    @NotNull
    private float tien_kham;
    @NotNull
    private String chuandoan;
    @NotNull
    private int songaytaikham;
    @NotNull
    private List<ThuocChoKhamBenhDTO> thuocChoKhamBenhDTOList;
}
