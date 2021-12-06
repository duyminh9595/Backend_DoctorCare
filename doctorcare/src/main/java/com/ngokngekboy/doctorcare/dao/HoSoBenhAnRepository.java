package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.HoSoBenhAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

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
}
