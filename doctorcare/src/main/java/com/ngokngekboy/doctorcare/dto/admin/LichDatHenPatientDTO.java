package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

@Data
public class LichDatHenPatientDTO {
    private Long id;
    private String ngaydat;
    private String buoi;
    private String usercancel;
    private String status;
}
