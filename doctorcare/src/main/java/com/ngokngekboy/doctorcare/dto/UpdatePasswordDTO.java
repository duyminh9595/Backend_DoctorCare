package com.ngokngekboy.doctorcare.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String oldpass;
    private String newpass;
}
