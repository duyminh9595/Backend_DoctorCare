package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "chi_tiet_nhap_kho")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ChiTietNhapKhoKey.class)
public class ChiTietNhapKho implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(nullable = false,name = "thuoc_id")
    private Thuoc thuoc;

    @Id
    @ManyToOne
    @JoinColumn(nullable = false,name = "nhapkhothuoc_id")
    private NhapKhoThuoc nhapkhothuoc;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private float price;
}
