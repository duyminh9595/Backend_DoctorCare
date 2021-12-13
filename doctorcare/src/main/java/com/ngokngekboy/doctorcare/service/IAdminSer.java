package com.ngokngekboy.doctorcare.service;

import com.ngokngekboy.doctorcare.dto.InfoMedicineDTO;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.NameMedicineDTOO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;
import com.ngokngekboy.doctorcare.dto.patient.TodayAppointment;

import java.util.List;

public interface IAdminSer {
    SuccessDTO AdminLogin(LoginDTO loginDTO);
    boolean AdminRegister(LoginDTO loginDTO);
    boolean  ThemBacSi(ThemBacSiDTO themBacSiDTO);
    ThemBacSiDTO  UpdateBacSi(ThemBacSiDTO themBacSiDTO);
    List<PatientInforDTO>GetDanhSachBenhNhan();
    PatientEditDTO GetInfoPatientEdit(Long id);

    boolean UpdateBenhNhanEdit(PatientEditDTO patientEditDTO);

    boolean DisableBenhNhan(Long id);

    List<PatientInforDTO> GetDanhSachBenhNhanByNameOrEmail(String name);

    boolean ThemLoaiThuocMoi(ThemThuocMoiDTO themThuocMoiDTO);

    boolean DisableThuoc(DisableThuocDTO disableThuocDTO);

    boolean AdminNhapKhoThuoc(List<ChiTietNhapKhoThuocDTO> chiTietNhapKhoThuocDTOList);

    boolean UpdatePriceThuoc(UpdatePriceDTO updatePriceDTO);

    List<AdminTodayAppointmentDTO> AdminGetTodayAppointment();

    boolean AdminHuyApointment(AdminHuyApointmentDTO adminHuyApointmentDTO);

    List<InfoMedicineDTO> GetMedicineByName(NameMedicineDTOO nameMedicineDTOO);

    List<InfoMedicineDTO> GetAllMedicine();

    List<TodayAppointment> GetApointmentFromToday();

    List<TodayAppointment> GetApointmentUserCancel();

    boolean AdminAproveApointmentUserCancel(Long id);

    List<TodayAppointment> GetApointmentUserCancelBaseOnEmail(String email);

    AllInforPatientDTO GetAllAboutPatient(Long id);

    HoSoBenhAnDTO GetALlAboutHoSoBenhAn(Long id);

    AllAboutDoctorDTO GetAllAboutDoctor(Long id);

    List<HoSoBenhAnDTO> GetAllHoSoBenhAnBaseDoctor(Long id);

    List<HoSoBenhAnDTO> GetAllHoSoBenhAnBaseDoctorDateRange(DateRangeDTO rangeDTO, Long id);

    boolean UpdateDoctorNewPass(DoctorNewPassDTO doctorNewPassDTO, Long id);

    List<HoSoBenhAnChuaTraTienDTO> GetChuaTraTienHoSoBenhAn();

    boolean BenhNhanThanhToanTien(Long id);

    List<DoanhThuDTO> GetDoanhThuTheoNgay();

    List<DoanhThuDTO> GetDoanhThuTheoThangHienTai();

    List<DoanhThuDTO> GetDoanhThuTheoNamHienTai();

    List<DoanhThuDTO> GetDoanhThuTheoDateRange(DateRangeDTO rangeDTO);

    List<HoSoBenhAnChuaTraTienDTO> GetChuaTraTienHoSoBenhAnBaseOnId(Long id);

    DashboardDTO GetTopDashboard();

    List<BacSiDoanhThuTrongThangDTO> GetTopDoanhThuTrongThang();

    List<ThuocTrongThangDTO> GetThuocDungTrongThang();

    List<CaBenhTrongNgayDTO> GetCaBenhTrongNgay();
}
