package com.ngokngekboy.doctorcare.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LichLamViecPatientAddApointmentDTO {
    private String date;
    private List<Boolean> session=new ArrayList<>();
}
