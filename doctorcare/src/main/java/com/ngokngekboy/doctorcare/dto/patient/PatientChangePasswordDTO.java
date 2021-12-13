package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

@Data
public class PatientChangePasswordDTO {
    private String oldpass;
    private String newpass;
}
