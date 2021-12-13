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
    private Long patentid;
    private int sdt;
    private String image_url;
    private boolean user_send_cancel;
    private boolean bs_da_chuandoan;
    private boolean status;
    private String emailbn;

}
