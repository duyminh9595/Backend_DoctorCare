package com.ngokngekboy.doctorcare.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ho_so_benh_an")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoSoBenhAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false,name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(nullable = false,name = "patent_id")
    private Patient patient;

    @Column(name = "date_created")
    private Date date_created;

    @Column(name = "chuan_doan",columnDefinition="TEXT")
    private String chuan_doan;

    @Column(name = "ngay_tai_kham")
    private Date ngay_tai_kham;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "hosobenhan")
    private List<HoaDonThuoc> hoaDonThuocList;

    @OneToOne
    @JoinColumn(name = "lichHenKham_id", nullable = false)
    private LichHenKham lichHenKham;

    @Column(name = "price_kham_benh")
    private float price_kham_benh;

    @Column(name = "thanh_toan")
    private boolean thanh_toan;


    public void AddHoaDonThuoc(HoaDonThuoc hoaDonThuoc)
    {
        if(hoaDonThuoc!=null)
        {
            if(hoaDonThuocList==null)
                hoaDonThuocList=new ArrayList<>();
            hoaDonThuocList.add(hoaDonThuoc);
            hoaDonThuoc.setHosobenhan(this);
        }
    }
}
