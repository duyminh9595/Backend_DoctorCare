package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

@Data
public class ChiTietNhapKhoThuocDTO {
    private Long thuocid;
    private float price;
    private Long quantity;
    private String name;
}
