package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

@Data
public class DoctorTodayApointmentDTO {
    private Long id;
    private Long patientid;
    private String patientname;
    private String namsinh;
    private String ngay;
    private String thoigian;
    private boolean buoi;
}
