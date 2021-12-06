package com.ngokngekboy.doctorcare.dto.patient;

import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.Patient;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class HoSoBenhAnDTO {
    private Long id;

    private String doctorname;


    private Date date_created;


    private String chuan_doan;


    private Date ngay_tai_kham;
}
