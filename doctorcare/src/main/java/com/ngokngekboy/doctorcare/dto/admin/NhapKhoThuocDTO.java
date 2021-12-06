package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NhapKhoThuocDTO {
    List<ChiTietNhapKhoThuocDTO> chiTietNhapKhoThuocDTOList=new ArrayList<>();
}
