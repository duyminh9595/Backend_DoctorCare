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
@Table(name = "thuoc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Thuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private float price;

    @Column(name = "last_update_price")
    private Date last_update_price;

    @Column(name = "last_update_quantity")
    private Date last_update_quantity;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "thuoc")
    private List<HoaDonThuoc> thuocList ;

    @Column(name = "reason")
    private String reason;

    @Column(name = "date_disable")
    private Date date_disable;

    @Column(name = "date_created")
    private Date date_created;


    @OneToOne
    @JoinColumn(name = "admin_update_id")
    private Admin admin_update;

    @ManyToOne
    @JoinColumn(nullable = false,name = "admin_id")
    private Admin admin;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "thuoc")
    private List<ChiTietNhapKho> chiTietNhapKhoList ;

    public void AddHoaDonThuoc(HoaDonThuoc hoaDonThuoc)
    {
        if(hoaDonThuoc!=null)
        {
            if(thuocList==null)
                thuocList=new ArrayList<>();
            thuocList.add(hoaDonThuoc);
            hoaDonThuoc.setThuoc(this);
        }
    }

    public void AddChiTietNhapKho(ChiTietNhapKho chiTietNhapKho)
    {
        if(chiTietNhapKho!=null)
        {
            if(chiTietNhapKhoList==null)
                chiTietNhapKhoList=new ArrayList<>();
            chiTietNhapKhoList.add(chiTietNhapKho);
            chiTietNhapKho.setThuoc(this);
        }
    }
}
