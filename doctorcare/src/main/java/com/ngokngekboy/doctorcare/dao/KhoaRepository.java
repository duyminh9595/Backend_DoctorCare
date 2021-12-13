package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.Khoa;
import com.ngokngekboy.doctorcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "khoa",collectionResourceRel = "khoa")
@CrossOrigin
public interface KhoaRepository extends JpaRepository<Khoa,Long> {
    @Query("select p from Khoa  p  ")
    List<Khoa>DanhSachKhoa();

    Optional<Khoa> findById(@Param("id")Long id);

    @Query("select p from Khoa p where p.tenkhoa like CONCAT('%',:name,'%')  ")
    List<Khoa>findKhoaByTen(String name);
}
