package com.ngokngekboy.doctorcare.service;

import com.ngokngekboy.doctorcare.dto.InfoMedicineDTO;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.NameMedicineDTOO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;

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
}
