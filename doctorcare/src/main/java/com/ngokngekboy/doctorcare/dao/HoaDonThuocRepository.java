package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.HoSoBenhAn;
import com.ngokngekboy.doctorcare.entity.HoaDonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "hoadonthuoc",collectionResourceRel = "hoadonthuoc")
@CrossOrigin
public interface HoaDonThuocRepository extends JpaRepository<HoaDonThuoc,Long> {

    @Query("select p from HoaDonThuoc p where p.hosobenhan.id=?1 ")
    List<HoaDonThuoc>findByHoSoBenhAnId(Long id);

    @Query("select p from HoaDonThuoc p where p.hosobenhan.doctor.id=?1 ")
    List<HoaDonThuoc>findByDoctorId(Long id);

    @Query("select p from HoaDonThuoc p where p.thuoc.id=?1 ")
    List<HoaDonThuoc>findByThuocId(Long id);

    @Query("select p from HoaDonThuoc p where p.thuoc.id=?1 and p.hosobenhan.doctor.id=?2 ")
    List<HoaDonThuoc>findByThuocIdAndDoctorId(Long id,Long doctorid);
}
