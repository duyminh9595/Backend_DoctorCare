package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import java.util.List;


@Data
public class AllInforPatientDTO {
    private Long id;
    private String fullname;
    private String email;
    private String ngaysinh;
    private String gioitinh;
    private String sdt;
    private String cmnd;
    private List<HoSoBenhAnDTO> hoSoBenhAnDTOList;
    private boolean status;
    private String imageurl;
    private String diachi;
    private List<LichDatHenPatientDTO>lichDatHenPatientDTOS;
}
