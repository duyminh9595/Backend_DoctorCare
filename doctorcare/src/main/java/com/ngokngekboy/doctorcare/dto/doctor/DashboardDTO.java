package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

@Data
public class DashboardDTO {
    private int todaypatient;
    private int monthpatient;
    private float percent_patient;

    private int todayappointment;
    private int monthappointment;
    private float percent_appointment;

    private int todaykhambenh;
    private int monthkhambenh;
    private float percent_khambenh;

    private  float earning;
    private float monthearning;
    private float percent_earning;
}
