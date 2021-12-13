package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

@Data
public class AllAboutDoctorDTO {
    private Long id;
    private String tenbacsi;
    private String tenkhoa;
    private String cmnd;
    private String bangcap;
    private String ngaysinh;
    private String email;
    private String gioitinh;
    private String image_url;
    private String sdt;
    private Long tongluotkhamtrongthang;
    private Long tongluotkhamtrongnam;
    private Long tongluotkham;
}
