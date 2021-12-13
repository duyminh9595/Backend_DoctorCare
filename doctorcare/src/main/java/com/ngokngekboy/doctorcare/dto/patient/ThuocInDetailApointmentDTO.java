package com.ngokngekboy.doctorcare.dto.patient;

import lombok.Data;

@Data
public class ThuocInDetailApointmentDTO {
    private String tenthuoc;
    private float price_per_quantity;
    private float giatien;
    private Long soluong;
}
