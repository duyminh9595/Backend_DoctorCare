package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.NhapKhoThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "nhapkho",collectionResourceRel = "nhapkho")
@CrossOrigin
public interface NhapKhoRepository extends JpaRepository<NhapKhoThuoc,Long> {
}
