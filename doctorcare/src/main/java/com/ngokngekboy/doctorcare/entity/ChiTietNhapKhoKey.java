package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietNhapKhoKey implements Serializable {

    private Long thuoc;
    private Long nhapkhothuoc;
}
