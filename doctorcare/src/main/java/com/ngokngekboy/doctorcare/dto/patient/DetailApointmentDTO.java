package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

import java.util.List;

@Data
public class DetailApointmentDTO {
    private String tenbacsi;
    private String ngaykham;
    private List<ThuocInDetailApointmentDTO> thuocdetail;
    private float tienkhambenh;
    private float tongbienlai;
    private boolean da_thanh_toan;
}
