package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

@Data
public class AdminTodayAppointmentDTO {
    private Long id;
    private Long bacsiid;
    private Long benhnhanid;
    private Long khoaid;
    private String bacsiname;
    private String tenkhoa;
    private String ngay;
    private String thoigian;
    private String buoi;
    private int sdtbenhnhan;
    private int sdtbs;
    private String tenbenhnhan;
}
