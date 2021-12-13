package com.ngokngekboy.doctorcare.controller.admin;

import com.ngokngekboy.doctorcare.dto.*;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.doctor.InfoDoctor;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;
import com.ngokngekboy.doctorcare.dto.patient.TodayAppointment;
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
    @GetMapping("/core/appointmentfromtoday")
    public ResponseEntity GetAppointmentFromToday()
    {
        List<TodayAppointment>todayAppointments=iAdminSer.GetApointmentFromToday();
        return ResponseEntity.ok(todayAppointments);
    }
    @GetMapping("/core/appointmentusercancel")
    public ResponseEntity GetAppointmentUserCancel()
    {
        List<TodayAppointment>todayAppointments=iAdminSer.GetApointmentUserCancel();
        return ResponseEntity.ok(todayAppointments);
    }
    @PostMapping("/core/adminaprovecancel")
    public ResponseEntity AdminAproveAppointmentUserCancel(@RequestParam Long id)
    {
        boolean check=iAdminSer.AdminAproveApointmentUserCancel(id);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PostMapping("/core/searchemailcancel")
    public ResponseEntity GetApointCancelBasePatientName(@RequestBody EmailPatientDTO emailPatientDTO)
    {
        List<TodayAppointment>todayAppointments=iAdminSer.GetApointmentUserCancelBaseOnEmail(emailPatientDTO.getEmail());
        return ResponseEntity.ok(todayAppointments);
    }
    @GetMapping("/core/getallaboutpatient")
    public ResponseEntity GetAllInforAboutPatient(@RequestParam Long id)
    {
        AllInforPatientDTO allInforPatientDTO=iAdminSer.GetAllAboutPatient(id);
        if(allInforPatientDTO!=null)
            return ResponseEntity.ok(allInforPatientDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/core/getinforabouthosobenhan")
    public ResponseEntity GetAllInforAboutHoSoBenhAN(@RequestParam Long id)
    {
        HoSoBenhAnDTO hoSoBenhAnDTO=iAdminSer.GetALlAboutHoSoBenhAn(id);
        if(hoSoBenhAnDTO!=null)
            return ResponseEntity.ok(hoSoBenhAnDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/core/getallaboutdoctor")
    public ResponseEntity GetAllInforAboutDoctor(@RequestParam Long id)
    {
        AllAboutDoctorDTO allAboutDoctorDTO=iAdminSer.GetAllAboutDoctor(id);
        if(allAboutDoctorDTO!=null)
            return ResponseEntity.ok(allAboutDoctorDTO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/core/getallhosobenhanbasedoctor")
    public ResponseEntity GetAllInforAboutDoctorHoSo(@RequestParam Long id)
    {
        List<HoSoBenhAnDTO>hoSoBenhAnDTOList=iAdminSer.GetAllHoSoBenhAnBaseDoctor(id);
        return ResponseEntity.ok(hoSoBenhAnDTOList);
    }
    @PostMapping("core/searchdaterange")
    public ResponseEntity GetAllInforAboutDoctorHoSoSearchDateRange(@RequestBody DateRangeDTO rangeDTO,@RequestParam Long id)
    {
        List<HoSoBenhAnDTO>hoSoBenhAnDTOList=iAdminSer.GetAllHoSoBenhAnBaseDoctorDateRange(rangeDTO,id);
        return ResponseEntity.ok(hoSoBenhAnDTOList);
    }
    @PostMapping("core/updatepassdoctor")
    public ResponseEntity UpdatePasswordDoctor(@RequestBody DoctorNewPassDTO doctorNewPassDTO,@RequestParam Long id)
    {
        boolean check=iAdminSer.UpdateDoctorNewPass(doctorNewPassDTO,id);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("core/getlisthosobenhanchuatratien")
    public ResponseEntity GetListHoSoBenhAnChuaTraTien(){
        List<HoSoBenhAnChuaTraTienDTO>hoSoBenhAnChuaTraTienDTOList=iAdminSer.GetChuaTraTienHoSoBenhAn();
        return ResponseEntity.ok(hoSoBenhAnChuaTraTienDTOList);
    }
    @GetMapping("core/getlisthosobenhanchuatratienbaseonid")
    public ResponseEntity GetListHoSoBenhAnChuaTraTien(@RequestParam Long id){
        List<HoSoBenhAnChuaTraTienDTO>hoSoBenhAnChuaTraTienDTOList=iAdminSer.GetChuaTraTienHoSoBenhAnBaseOnId(id);
        return ResponseEntity.ok(hoSoBenhAnChuaTraTienDTOList);
    }
    @PostMapping("core/benhnhanthanhtoantien")
    public ResponseEntity BenhNhanThanhToanTien(@RequestParam Long id)
    {
        boolean check=iAdminSer.BenhNhanThanhToanTien(id);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/core/getdoanhthutheongayhientai")
    public ResponseEntity DoanhThuTheoNgayHienTai()
    {
        List<DoanhThuDTO>doanhThuDTOS=iAdminSer.GetDoanhThuTheoNgay();
        return ResponseEntity.ok(doanhThuDTOS);
    }
    @GetMapping("/core/getdoanhthutheothanghientai")
    public ResponseEntity DoanhThuTheoThangHienTai()
    {
        List<DoanhThuDTO>doanhThuDTOS=iAdminSer.GetDoanhThuTheoThangHienTai();
        return ResponseEntity.ok(doanhThuDTOS);
    }
    @GetMapping("/core/getdoanhthutheonamhientai")
    public ResponseEntity DoanhThuTheoNamHienTai()
    {
        List<DoanhThuDTO>doanhThuDTOS=iAdminSer.GetDoanhThuTheoNamHienTai();
        return ResponseEntity.ok(doanhThuDTOS);
    }
    @PostMapping("/core/getdoanhthutheodaterange")
    public ResponseEntity DoanhThuTheoDateRange(@RequestBody DateRangeDTO rangeDTO)
    {
        List<DoanhThuDTO>doanhThuDTOS=iAdminSer.GetDoanhThuTheoDateRange(rangeDTO);
        return ResponseEntity.ok(doanhThuDTOS);
    }
    @GetMapping("/core/dashboard")
    public ResponseEntity Dashboard()
    {
        DashboardDTO dashboardDTO=iAdminSer.GetTopDashboard();
        return ResponseEntity.ok(dashboardDTO);
    }
    @GetMapping("/core/bacsinhieunhattrongthang")
    public ResponseEntity GetBacSiNhieuNhatTrongThang()
    {
        List<BacSiDoanhThuTrongThangDTO>bacSiDoanhThuTrongThangDTOS=iAdminSer.GetTopDoanhThuTrongThang();
        return ResponseEntity.ok(bacSiDoanhThuTrongThangDTOS);
    }
    @GetMapping("/core/thuoctrongthang")
    public ResponseEntity GetThuocTrongThang()
    {
        List<ThuocTrongThangDTO>thuocTrongThangDTOS=iAdminSer.GetThuocDungTrongThang();
        return ResponseEntity.ok(thuocTrongThangDTOS);
    }
    @GetMapping("/core/cabenhtrongngay")
    public ResponseEntity GetCaBenhTrongNgay()
    {
        List<CaBenhTrongNgayDTO>caBenhTrongNgayDTOS=iAdminSer.GetCaBenhTrongNgay();
        return ResponseEntity.ok(caBenhTrongNgayDTOS);
    }
    @GetMapping("/core/getdanhsachbacsitrongkhoa")
    public ResponseEntity GetBacSiTrongKhoa(@RequestParam Long id)
    {
        List<InfoDoctor>infoDoctorList=iAdminSer.GetDanhSachDocTorBaseKhoa(id);
        return ResponseEntity.ok(infoDoctorList);
    }
    @PostMapping("/core/getbacsibyname")
    public ResponseEntity GetBacSiByName(@RequestBody FindDoctorNameDTO findDoctorNameDTO,@RequestParam Long id)
    {
        List<InfoDoctor>infoDoctorList=iAdminSer.GetDanhSachDocTorByName(findDoctorNameDTO.getName(),id);
        return ResponseEntity.ok(infoDoctorList);
    }
}
