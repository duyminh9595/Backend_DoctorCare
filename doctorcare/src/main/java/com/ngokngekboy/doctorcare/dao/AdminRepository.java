package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Admin;
import com.ngokngekboy.doctorcare.entity.Khoa;
import com.ngokngekboy.doctorcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "admin",collectionResourceRel = "admin")
@CrossOrigin
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByEmail(String email);

    @Query("select p from Admin  p  where p.id=?1")
    Admin findAdminById(Long id);
}
