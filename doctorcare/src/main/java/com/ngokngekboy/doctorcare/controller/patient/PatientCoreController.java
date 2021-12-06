package com.ngokngekboy.doctorcare.controller.patient;


import com.ngokngekboy.doctorcare.common.DateFormat;
import com.ngokngekboy.doctorcare.dto.DoctorBaseOnKhoaIdDTO;
import com.ngokngekboy.doctorcare.dto.LichLamViecPatientAddApointmentDTO;
import com.ngokngekboy.doctorcare.dto.ListDanhSachBacSiFree3DayDTO;
import com.ngokngekboy.doctorcare.dto.patient.*;
import com.ngokngekboy.doctorcare.entity.HoSoBenhAn;
import com.ngokngekboy.doctorcare.entity.LichHenKham;
import com.ngokngekboy.doctorcare.service.IPatientSer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/patient/core")
public class PatientCoreController {

    private IPatientSer iPatientSer;
    private DateFormat dateFormat;

    @GetMapping("/checktoken")
    public ResponseEntity CheckTokenLogin()
    {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/doctorbaseonkhoaid")
    public ResponseEntity ListDoctorBaseOnKhoaId(@RequestParam Long id)
    {
        List<DoctorBaseOnKhoaIdDTO>doctorBaseOnKhoaIdDTOS=iPatientSer.GetDanhSachBacSiBaseOnKhoaId(id);
        return ResponseEntity.ok(doctorBaseOnKhoaIdDTOS);
    }

    @GetMapping("/hosobenhan")
    public ResponseEntity HoSoBenhAnBenhNhan()
    {
        List<HoSoBenhAn>hoSoBenhAns=new ArrayList<>();
        hoSoBenhAns=iPatientSer.DanhSachHoSoBenhAn();
        List<HoSoBenhAnDTO>hoSoBenhAnDTOS=new ArrayList<>();
        HoSoBenhAnDTO hoSoBenhAnDTO;
        for(HoSoBenhAn hoSoBenhAn:hoSoBenhAns)
        {
            hoSoBenhAnDTO=new HoSoBenhAnDTO();

            hoSoBenhAnDTO.setChuan_doan(hoSoBenhAn.getChuan_doan());
            hoSoBenhAnDTO.setDoctorname(hoSoBenhAn.getDoctor().getFullName());
            hoSoBenhAnDTO.setDate_created(hoSoBenhAn.getDate_created());
            hoSoBenhAnDTO.setId(hoSoBenhAn.getId());
            hoSoBenhAnDTO.setNgay_tai_kham(hoSoBenhAn.getNgay_tai_kham());

            hoSoBenhAnDTOS.add(hoSoBenhAnDTO);
        }
        return ResponseEntity.ok(hoSoBenhAnDTOS);
    }
    @PostMapping("/datlichkham")
    public ResponseEntity DatLichKham(@RequestBody DatLichKhamDTO datLichKhamDTO)
    {
        boolean check=iPatientSer.DatLichKham(datLichKhamDTO);
        if(check)
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/lichsudatlichkham")
    public  List<LichHenKhamDTO> LichSuDatLichKham()
    {
        List<LichHenKham>lichHenKhams=iPatientSer.LichHenKhamBenhNhan();
        LichHenKhamDTO lichHenKhamDTO;
        List<LichHenKhamDTO>lichHenKhamDTOS=new ArrayList<>();
        for (LichHenKham data:lichHenKhams)
        {
            lichHenKhamDTO=new LichHenKhamDTO();
            lichHenKhamDTO.setBuoi(data.isBuoi());
            lichHenKhamDTO.setDate(dateFormat.ConverDateToString(data.getDate_created()));
            lichHenKhamDTO.setStt(data.getStt());
            lichHenKhamDTO.setId(data.getId());
            lichHenKhamDTO.setStatus(data.isStatus());
            lichHenKhamDTOS.add(lichHenKhamDTO);
        }

        return lichHenKhamDTOS;
    }
    @GetMapping("/lichlamviecbacsi")
    public ResponseEntity GetLichLamViecPatientAddApointment(@RequestParam Long bacsiid)
    {
        List<LichLamViecPatientAddApointmentDTO>lichLamViecPatientAddApointmentDTOS=iPatientSer.GetLichLamViecBacSiPatientAddApointment(bacsiid);
        return ResponseEntity.ok(lichLamViecPatientAddApointmentDTOS);
    }
    @GetMapping("/todayappointment")
    public ResponseEntity GetTodayAppointment()
    {
        List<TodayAppointment>todayAppointments=iPatientSer.GetTodayAppointment();
        return ResponseEntity.ok(todayAppointments);
    }
    @PostMapping("/huyappointment")
    public ResponseEntity Huyappointment(@RequestParam Long id)
    {
        boolean check=iPatientSer.HuyAppointment(id);
        if(check)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/danhsachbacsifree3day")
    public ResponseEntity DanhSachBacSiFree3Day()
    {
        List<ListDanhSachBacSiFree3DayDTO>danhSachBacSiFree3DayDTOS=iPatientSer.GetDanhSachBacSiFree3Day();
        return ResponseEntity.ok(danhSachBacSiFree3DayDTOS);
    }
    @GetMapping("/gethistoryapointment")
    public ResponseEntity  GetHistoryApointment()
    {
        List<LichSuApointmentDTO>lichSuApointmentDTOList=iPatientSer.GetLichSuApointment();
        return ResponseEntity.ok(lichSuApointmentDTOList);
    }
}
