package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class HoSoBenhAnDTO {
    private Long id;
    private String tenbacsi;
    private Long idbacsi;
    private String ngaykham;
    private String ngaytaikham;
    private String giatienkhambenh;
    private String dathanhtoan;
    private String chuandoan;
    private String tongcongtienthuoc;
    private String giatienphaitra;
    private String tenbenhnhan;
    private String sdtbenhnhan;
    private String buoi;
    private boolean status;
    private List<ThuocInHoSoBenhAnDTO> thuocInHoSoBenhAnDTOList;
}
