package com.ngokngekboy.doctorcare.dto;

import lombok.Data;

@Data
public class InfoMedicineDTO {
    private Long id;
    private Long quantity;
    private float price;
    private String name;
    private boolean status;
}
