package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.ChiTietNhapKho;
import com.ngokngekboy.doctorcare.entity.ChiTietNhapKhoKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "chitietnhapkho",collectionResourceRel = "chitietnhapkho")
@CrossOrigin
public interface ChiTietNhapKhoRepository extends JpaRepository<ChiTietNhapKho, Long> {
}
