package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

@Data
public class PatientInforDTO {
    private Long id;
    private String fullName;
    private String email;
    private boolean enable_status;
    private boolean gender;
    private int sdt;
}
