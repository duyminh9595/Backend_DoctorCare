package com.ngokngekboy.doctorcare.service;

import com.ngokngekboy.doctorcare.dto.*;
import com.ngokngekboy.doctorcare.dto.patient.*;
import com.ngokngekboy.doctorcare.entity.HoSoBenhAn;
import com.ngokngekboy.doctorcare.entity.LichHenKham;
import com.ngokngekboy.doctorcare.entity.Patient;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IPatientSer {
    boolean PatientRegister(PatientRegisterDTO patientRegisterDTO);
    SuccessDTO PatientLogin(LoginDTO loginDTO);
    boolean SendEmailConfirm(PatientRegisterDTO patientRegisterDTO);
    boolean ConfirmEmail(String token);
    List<HoSoBenhAn> DanhSachHoSoBenhAn();
    boolean DatLichKham(DatLichKhamDTO datLichKhamDTO);
    List<LichHenKham>LichHenKhamBenhNhan();
    boolean EmailLayLaiMatKhau(String email);
    boolean ForgotPassword(String token, UpdatePasswordDTO updatePasswordDTO);


    List<DoctorBaseOnKhoaIdDTO> GetDanhSachBacSiBaseOnKhoaId(Long id);

    List<LichLamViecPatientAddApointmentDTO> GetLichLamViecBacSiPatientAddApointment(Long bacsiid);

    List<TodayAppointment> GetTodayAppointment();

    boolean HuyAppointment(Long id);

    List<ListDanhSachBacSiFree3DayDTO> GetDanhSachBacSiFree3Day();

    List<LichSuApointmentDTO> GetLichSuApointment();
}
