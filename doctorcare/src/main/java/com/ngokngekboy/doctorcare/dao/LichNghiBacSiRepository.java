package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.LichNghiBacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "lichnghibacsi",collectionResourceRel = "lichnghibacsi")
@CrossOrigin
public interface LichNghiBacSiRepository extends JpaRepository<LichNghiBacSi,Long> {

    @Query("select p from LichNghiBacSi p where p.doctor.email=?1 ")
    List<LichNghiBacSi> SearchBaseOnEmailAndDate(String email, Date date);

    @Query("select p from LichNghiBacSi p where p.date_off BETWEEN ?1 AND ?2 ")
    List<LichNghiBacSi> LichNghiBacSiBetween2day(Date firstDate, Date lastDate);
}
