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
@Table(name = "nhap_kho_thuoc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhapKhoThuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "date_created")
    private Date date_created;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "nhapkhothuoc")
    private List<ChiTietNhapKho> chiTietNhapKhoList ;

    public void NhapKho(ChiTietNhapKho chiTietNhapKho)
    {
        if(chiTietNhapKho!=null)
        {
            if(chiTietNhapKhoList==null)
                chiTietNhapKhoList=new ArrayList<>();
            chiTietNhapKhoList.add(chiTietNhapKho);
            chiTietNhapKho.setNhapkhothuoc(this);
        }
    }
}
