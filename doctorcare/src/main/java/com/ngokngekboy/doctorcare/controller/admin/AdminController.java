package com.ngokngekboy.doctorcare.controller.admin;

import com.ngokngekboy.doctorcare.dto.*;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;
import com.ngokngekboy.doctorcare.jwt.JwtGenerationAdmin;
import com.ngokngekboy.doctorcare.service.IAdminSer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private IAdminSer iAdminSer;
    private JwtGenerationAdmin jwtGenerationAdmin;

    @PostMapping("/register")
    public ResponseEntity AdminRegister(@RequestBody LoginDTO loginDTO)
    {
        boolean check=iAdminSer.AdminRegister(loginDTO);
        if(!check)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity AdminLogin(@RequestBody LoginDTO loginDTO) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SuccessDTO successDTO=iAdminSer.AdminLogin(loginDTO);
        if(successDTO!=null)
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", successDTO.getToken());
            headers.add("Position",successDTO.getEmail());
            return ResponseEntity.ok(successDTO);
//            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/checktokenvalid")
    public ResponseEntity AdminCheckTokenValid ()
    {
        return ResponseEntity.ok().build();
    }
    @PostMapping("/core/thembacsi")
    public ResponseEntity AdminThemBacSi(@RequestBody ThemBacSiDTO themBacSiDTO) {
        boolean check = iAdminSer.ThemBacSi(themBacSiDTO);
        if (check) {
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/updatebacsi")
    public ResponseEntity AdminUpdateBacSi(@RequestBody ThemBacSiDTO themBacSiDTO) {
        ThemBacSiDTO check = iAdminSer.UpdateBacSi(themBacSiDTO);
        if (check!=null) {
            return ResponseEntity.ok(check);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/core/danhsachbenhnhan")
    public ResponseEntity AdminDanhSachBenhNhan()
    {
        List<PatientInforDTO> patientInforDTOList=iAdminSer.GetDanhSachBenhNhan();
        return ResponseEntity.ok(patientInforDTOList);
    }
    @GetMapping("/core/getbenhnhanedit")
    public ResponseEntity AdminGetBenhNhanEdit(@RequestParam Long id)
    {
        PatientEditDTO patientEditDTO= iAdminSer.GetInfoPatientEdit(id);
        if(patientEditDTO!=null)
        {
            return ResponseEntity.ok(patientEditDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/updatebenhnhanedit")
    public ResponseEntity AdminUpdateBenhNhanEdit(@RequestBody PatientEditDTO patientEditDTO)
    {
        boolean check=iAdminSer.UpdateBenhNhanEdit(patientEditDTO);
        if(check)
        {
            return ResponseEntity.ok(patientEditDTO);
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/disablebenhnhan")
    public ResponseEntity AdminDisableBenhNhan(@RequestParam Long id)
    {
        boolean check=iAdminSer.DisableBenhNhan(id);
        if(check)
        {
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/danhsachbenhnhanbynameemail")
    public ResponseEntity AdminDanhSachBenhNhan(@RequestBody FindPatientNameDTO findPatientNameDTO)
    {
        List<PatientInforDTO> patientInforDTOList=iAdminSer.GetDanhSachBenhNhanByNameOrEmail(findPatientNameDTO.getName());
        return ResponseEntity.ok(patientInforDTOList);
    }
    @PostMapping("/core/themthuoc")
    public ResponseEntity AdminThemLoaiThuoc(@RequestBody ThemThuocMoiDTO themThuocMoiDTO)
    {
        boolean check=iAdminSer.ThemLoaiThuocMoi(themThuocMoiDTO);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/diablethuoc")
    public ResponseEntity AdminDisableLoaiThuoc(@RequestBody DisableThuocDTO disableThuocDTO)
    {
        boolean check=iAdminSer.DisableThuoc(disableThuocDTO);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/addnhapkhothuoc")
    public ResponseEntity AdminNhapKhoThuoc(@RequestBody List<ChiTietNhapKhoThuocDTO> chiTietNhapKhoThuocDTOList)
    {
        boolean check=iAdminSer.AdminNhapKhoThuoc(chiTietNhapKhoThuocDTOList);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/updateprice")
    public ResponseEntity AdminUpdatePriceThuoc(@RequestBody UpdatePriceDTO updatePriceDTO)
    {
        boolean check=iAdminSer.UpdatePriceThuoc(updatePriceDTO);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/appointmentoday")
    public ResponseEntity AdminDatLichHenDumPatient()
    {
        List<AdminTodayAppointmentDTO> adminTodayAppointmentDTOS =iAdminSer.AdminGetTodayAppointment();
        return ResponseEntity.ok(adminTodayAppointmentDTOS);
    }
    @PostMapping("/core/disableappointmentbyadmin")
    public ResponseEntity AdminHuyApointment(@RequestBody AdminHuyApointmentDTO adminHuyApointmentDTO)
    {
        boolean check=iAdminSer.AdminHuyApointment(adminHuyApointmentDTO);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/getinfomedicinebyname")
    public ResponseEntity AdminFindMedicineByName(@RequestBody NameMedicineDTOO nameMedicineDTOO)
    {
        List<InfoMedicineDTO>infoMedicineDTOList=iAdminSer.GetMedicineByName(nameMedicineDTOO);
        return ResponseEntity.ok(infoMedicineDTOList);
    }
    @GetMapping("/core/getallmedicine")
    public ResponseEntity AdminGetAllMedicine()
    {
        List<InfoMedicineDTO>infoMedicineDTOList=iAdminSer.GetAllMedicine();
        return ResponseEntity.ok(infoMedicineDTOList);
    }
}
