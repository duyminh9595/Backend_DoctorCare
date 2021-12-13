package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

@Data
public class ThuocTrongThangDTO {
    private Long id;
    private String tenthuoc;
    private Long quantity;
    private float price;
}
