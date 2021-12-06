package com.ngokngekboy.doctorcare.dto.patient;


import lombok.Data;

@Data
public class LichSuApointmentDTO {
    private Long id;
    private boolean status;
    private String date;
    private String gio;
    private String tenbs;
}
