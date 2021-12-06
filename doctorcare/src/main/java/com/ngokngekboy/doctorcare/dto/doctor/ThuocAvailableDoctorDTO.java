package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

@Data
public class ThuocAvailableDoctorDTO {
    private Long id;
    private Long quantity;
    private String name;
    private float price;
}
