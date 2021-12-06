package com.ngokngekboy.doctorcare.controller.khoa;


import com.ngokngekboy.doctorcare.dao.KhoaRepository;
import com.ngokngekboy.doctorcare.dto.KhoaDTO;
import com.ngokngekboy.doctorcare.dto.admin.ThemKhoaDTO;
import com.ngokngekboy.doctorcare.dto.patient.PatientRegisterDTO;
import com.ngokngekboy.doctorcare.entity.Khoa;
import com.ngokngekboy.doctorcare.jwt.JwtGenerationAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/khoa")
public class KhoaController {

    private KhoaRepository khoaRepository;
    private JwtGenerationAdmin jwtGenerationAdmin;

    @PostMapping("/themmoi")
    public ResponseEntity Register(@RequestBody ThemKhoaDTO themKhoaDTO)
    {
        Khoa khoa=new Khoa();
        khoa.setId(0l);
        khoa.setTenkhoa(themKhoaDTO.getTenkhoa());
        khoaRepository.save(khoa);
        if(khoa!=null)
        {
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/danhsachkhoa")
    public ResponseEntity DanhSachKhoa()
    {
        List<Khoa> khoaList=khoaRepository.DanhSachKhoa();
        List<KhoaDTO>khoaDTOList=new ArrayList<>();
        for(Khoa data:khoaList)
        {
            KhoaDTO khoaDTO=new KhoaDTO();
            khoaDTO.setId(data.getId());
            khoaDTO.setTenkhoa(data.getTenkhoa());
            khoaDTOList.add(khoaDTO);
        }
        return ResponseEntity.ok(khoaDTOList);
    }
}
