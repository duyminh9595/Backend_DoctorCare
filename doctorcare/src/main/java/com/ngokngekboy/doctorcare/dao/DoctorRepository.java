package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Admin;
import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "doctor",collectionResourceRel = "doctor")
@CrossOrigin
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    @Query("select p from Doctor  p  where p.id=?1")
    Doctor findDoctorById(Long id);

    @Query("select p from Doctor  p")
    List<Doctor> findAllDoctor();

    Doctor findByEmail(@Param("email")String email);

    @Query("select p from Doctor p  where p.token_confirm_email=?1")
    Doctor findByToken_confirm_email(String token_confirm_email);

    @Query("select p from Doctor p where p.enable_status=true ")
    List<Doctor>findDanhSachDoctor();

    @Query("select p from Doctor p where p.fullName like CONCAT('%',:name_or_emaIl,'%')  or p.email like CONCAT('%',:name_or_emaIl,'%') ")
    List<Doctor>findDanhSachDoctorByName(String name_or_emaIl);

    @Query("select  p from Doctor p where p.khoa.id=?1 and p.enable_status=?2")
    List<Doctor>findDoctorsByOnKhoaId(Long id,boolean status);
}
