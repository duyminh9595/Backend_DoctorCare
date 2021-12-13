package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

@Data
public class BenhNhanTungKhamBenhDTO {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private String sdt;
    private boolean status;
}
