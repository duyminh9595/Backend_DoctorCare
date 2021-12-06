package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hoa_don_thuoc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(HoaDonThuocKey.class)
public class HoaDonThuoc implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(nullable = false,name = "thuoc_id")
    private Thuoc thuoc;

    @Id
    @ManyToOne
    @JoinColumn(nullable = false,name = "hosobenhan_id")
    private HoSoBenhAn hosobenhan;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price_per_quantity")
    private float price_per_quantity;
}
