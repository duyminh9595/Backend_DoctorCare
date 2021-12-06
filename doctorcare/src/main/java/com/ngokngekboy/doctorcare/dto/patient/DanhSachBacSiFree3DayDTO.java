package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

import java.util.List;

@Data
public class DanhSachBacSiFree3DayDTO {

    private String day;
    private List<Boolean> session;

}
