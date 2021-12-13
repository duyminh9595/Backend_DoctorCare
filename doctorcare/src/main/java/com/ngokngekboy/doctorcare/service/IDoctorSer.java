package com.ngokngekboy.doctorcare.service;

import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.UpdatePasswordDTO;
import com.ngokngekboy.doctorcare.dto.doctor.*;
import com.ngokngekboy.doctorcare.dto.patient.DetailApointmentDTO;

import java.util.List;

public interface IDoctorSer {
    SuccessDTO DoctorLogin(LoginDTO loginDTO);
    boolean SendResetPasswordDoctor(String email);
    boolean ForgotPassword(String token, UpdatePasswordDTO updatePasswordDTO);

    List<InfoDoctor> GetDanhSachDocTor();

    InfoDoctor GetDoctorEditId(Long id);

    List<InfoDoctor> GetDanhSachDocTorByName(String name);

    boolean BacSiXinNghiPhep(BacSiXinNghiDTO bacSiXinNghiDTO);

    List<DoctorTodayApointmentDTO> GetTodayApointmentDoctor();

    boolean KhamBenhAndChoThuoc(KhamBenhDTO khamBenhDTO,Long lichdatkhamid);

    List<ThuocAvailableDoctorDTO> GetThuocAvailableDoctor();

    List<ThuocAvailableDoctorDTO> GetThuocAvailableDoctorByName(NameThuocDoctorDTO nameThuocDoctorDTO);

    List<BenhNhanTungKhamBenhDTO> GetBenhNhanTungKhamBenh();

    List<BenhNhanTungKhamBenhDTO> GetBenhNhanTungKhamBenhTheoTenOrEmail(String name_or_email);

    List<HoSoBenhAnDTO> GetHoSoBenhAnBaseOnPatientId(Long id);

    DetailApointmentDTO GetDetailApointment(Long id);

    DashboardDTO GetDashboard();

    List<TopBenhNhanTrongThangDTO> GetTopBenhNhanTrongThang();

    List<TopThuocTrongThangDTO> GetThuocTrongThang();
}
