package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Patient;
import com.ngokngekboy.doctorcare.entity.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "thuoc",collectionResourceRel = "thuoc")
@CrossOrigin
public interface ThuocRepository extends JpaRepository<Thuoc,Long> {
    @Query("select p from Thuoc  p  where p.id=?1")
    Thuoc findThuocById(Long thuocid);

    @Query("select p from Thuoc p where p.name like CONCAT('%',:name,'%')  and p.status=true ")
    List<Thuoc> findThuocByName(String name);

    @Query("select p from Thuoc p where p.name like CONCAT('%',:name,'%')  ")
    List<Thuoc> findThuocByNameInAdmin(String name);

    @Query("select p from Thuoc p  ")
    List<Thuoc>GetAllMedicine();

    @Query("select p from Thuoc p where p.status=true and p.quantity>0")
    List<Thuoc>GetThuocAvailable();

    @Query("select p from Thuoc p where p.name like CONCAT('%',:name,'%')  and p.status=true and p.quantity>0")
    List<Thuoc> GetThuocAvailableByName(String name);
}
