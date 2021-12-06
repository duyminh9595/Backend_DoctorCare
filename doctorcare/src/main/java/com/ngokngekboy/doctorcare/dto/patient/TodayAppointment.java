package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

@Data
public class TodayAppointment {
    private Long id;
    private Long bacsiid;
    private String bacsiname;
    private String tenkhoa;
    private String ngay;
    private String thoigian;
    private String buoi;
    private int sdt;
    private String image_url;
}
