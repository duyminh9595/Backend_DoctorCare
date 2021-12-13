package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.HoSoBenhAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "hosobenhan",collectionResourceRel = "hosobenhan")
@CrossOrigin
public interface HoSoBenhAnRepository extends JpaRepository<HoSoBenhAn,Long> {

    @Query("select p from HoSoBenhAn  p  where p.patient.email=?1")
    List<HoSoBenhAn>findHoSoBenhAn(String email);

    @Query("select p from HoSoBenhAn  p  where p.lichHenKham.id=?1 and p.patient.id=?2")
    HoSoBenhAn findHoSoBenhAnByLichHenId(Long lichhenid,Long patientid);

    @Query("select p from HoSoBenhAn  p where p.lichHenKham.id=?1 and p.lichHenKham.status=?2")
    HoSoBenhAn findLichHenKhamIdExistInHoSoBenhAn(Long lichhenid,boolean status);

    @Query("select p from HoSoBenhAn p order by p.date_created desc ")
    Page<HoSoBenhAn>getAllByDate(Pageable pageable);

    @Query("select p from HoSoBenhAn  p where p.id=?1 ")
    HoSoBenhAn findHoSoBenhAnById(Long hosobenhanid);

    @Query("select p from HoSoBenhAn p where p.lichHenKham.id=?1 order by p.date_created")
    HoSoBenhAn findHoSoBenhAnhByLichHenId(Long id);

    @Query("select p from HoSoBenhAn p where p.patient.id=?1 order by p.date_created")
    List<HoSoBenhAn> findHoSoBenhAnhByPatientId(Long id);


    @Query("select p from HoSoBenhAn p where p.patient.id=?1 and p.doctor.id=?2 order by p.date_created")
    List<HoSoBenhAn> findHoSoBenhAnhByPatientIdAndDoctorId(Long id,Long doctorid);

    @Query("select p from HoSoBenhAn  p where p.doctor.id=?1 and p.thanh_toan=true ")
    List<HoSoBenhAn>findHoSoBenhAnByDoctor(Long id);

    @Query("select p from HoSoBenhAn  p where p.doctor.id=:id and p.thanh_toan=true and p.date_created BETWEEN :startDate AND :endDate ")
    List<HoSoBenhAn>findHoSoBenhAnByDoctorDạteRange(@Param("id")Long id, @Param("startDate")Date startDate, @Param("endDate")Date endDate);

    @Query("select p from HoSoBenhAn  p where p.thanh_toan=false ")
    List<HoSoBenhAn>findHoSoBenhAnChuaTraTien();

    @Query("select p from HoSoBenhAn  p where p.id =?1  and p.thanh_toan=false ")
    List<HoSoBenhAn>findHoSoBenhAnChuaTraTienBaseOnId(Long id);

    @Query("select p from HoSoBenhAn  p ")
    List<HoSoBenhAn>findAllHoSoBenhAn();

    @Query("select p from HoSoBenhAn  p where p.lichHenKham.id=?1 ")
    List<HoSoBenhAn>findHoSoBenhAnBaseOnLichHenKham(Long id);

    @Query("select p from HoSoBenhAn  p where p.doctor.id=?1 and p.patient.id=?2 and p.thanh_toan=true ")
    List<HoSoBenhAn>findAllHoSoBenhAnBaseOnBacSiIdAndPatientId(Long bacsiid,Long patientid);
}
