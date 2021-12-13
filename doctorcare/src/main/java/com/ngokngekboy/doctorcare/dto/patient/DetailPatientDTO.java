package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

@Data
public class DetailPatientDTO {
    private String fullname;
    private String ngaysinh;
    private String gioitinh;
    private String diachi;
    private String cmnd;
    private String image_url;
}
