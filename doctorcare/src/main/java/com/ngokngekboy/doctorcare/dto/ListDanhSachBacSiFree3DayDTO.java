package com.ngokngekboy.doctorcare.dto;

import com.ngokngekboy.doctorcare.dto.patient.DanhSachBacSiFree3DayDTO;
import lombok.Data;

import java.util.List;

@Data
public class ListDanhSachBacSiFree3DayDTO {
    private Long bacsiid;
    private Long khoaid;
    private String khoaname;
    private String bacsiname;
    private List<DanhSachBacSiFree3DayDTO> danhSachBacSiFree3DayDTOList;
}
