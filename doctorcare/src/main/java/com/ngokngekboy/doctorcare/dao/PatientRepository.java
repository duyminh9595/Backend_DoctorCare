package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "paitent",collectionResourceRel = "patient")
@CrossOrigin
public interface PatientRepository extends JpaRepository<Patient,Long> {
    Patient findByEmail(@Param("email")String email);
    @Query("select p from Patient  p  where p.token_confirm_email=?1")
    Patient findByToken_confirm_email(String token_confirm_email);

    @Query("select p from Patient  p  where p.id=?1")
    Patient findPatientById(Long id);


    @Query("select p from Patient  p  ")
    List<Patient>GetDanhSachBenhNhan();


    @Query("select p from Patient p where p.fullName like CONCAT('%',:name_or_emaIl,'%')  or p.email like CONCAT('%',:name_or_emaIl,'%') ")
    List<Patient>findDanhSachPatientByName(String name_or_emaIl);



}
