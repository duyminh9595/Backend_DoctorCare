package com.ngokngekboy.doctorcare.service;

import com.ngokngekboy.doctorcare.common.DateFormat;
import com.ngokngekboy.doctorcare.dao.*;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.UpdatePasswordDTO;
import com.ngokngekboy.doctorcare.dto.admin.ThuocTrongThangDTO;
import com.ngokngekboy.doctorcare.dto.doctor.*;
import com.ngokngekboy.doctorcare.dto.patient.DetailApointmentDTO;
import com.ngokngekboy.doctorcare.dto.patient.ThuocInDetailApointmentDTO;
import com.ngokngekboy.doctorcare.entity.*;
import com.ngokngekboy.doctorcare.jwt.JwtGenerationDoctor;
import com.ngokngekboy.doctorcare.sendemail.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.print.Doc;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DoctorSerImpl implements IDoctorSer{
    private DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtGenerationDoctor jwtGenerationDoctor;
    private final EmailSenderService emailSenderService;
    private final LichNghiBacSiRepository lichNghiBacSiRepository;
    private final LichKhamRepository lichKhamRepository;
    private final HoSoBenhAnRepository hoSoBenhAnRepository;
    private final PatientRepository patientRepository;
    private final ThuocRepository thuocRepository;
    private final HoaDonThuocRepository hoaDonThuocRepository;
    private DateFormat dateFormat;
    @Override
    public SuccessDTO DoctorLogin(LoginDTO loginDTO) {
        Doctor doctor=doctorRepository.findByEmail(loginDTO.getEmail());
        if(doctor!=null && doctor.isEnable_status())
        {
            if(passwordEncoder.matches(loginDTO.getPassword(),doctor.getPassword()))
            {
                SuccessDTO successDTO=new SuccessDTO();
                try {
                    successDTO.setToken(jwtGenerationDoctor.tokenJwtDoctor(doctor));
                    successDTO.setEmail(loginDTO.getEmail());
                    successDTO.setImage_url(doctor.getImage_url());
                    return successDTO;
                } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean SendResetPasswordDoctor(String email) {
        Doctor doctor=doctorRepository.findByEmail(email);
        if(doctor!=null && doctor.isEnable_status())
        {
            String token_confirm_email= UUID.randomUUID().toString();
            doctor.setToken_confirm_email(token_confirm_email);
            doctorRepository.save(doctor);
            emailSenderService.sendSimpleMessage(doctor.getEmail(),"Day la do ngok","Day la token: "+token_confirm_email);
            return true;
        }
        return false;
    }

    @Override
    public boolean ForgotPassword(String token, UpdatePasswordDTO updatePasswordDTO) {
        Doctor doctor= doctorRepository.findByToken_confirm_email(token);
        if(doctor!=null)
        {
            if(updatePasswordDTO.getNewpass().equals(updatePasswordDTO.getOldpass()))
            {
                doctor.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewpass()));
                doctor.setToken_confirm_email("");
                doctorRepository.save(doctor);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<InfoDoctor> GetDanhSachDocTor() {
        List<Doctor>doctorList= doctorRepository.findDanhSachDoctor();
        List<InfoDoctor>infoDoctorList=new ArrayList<>();
        for(Doctor data:doctorList)
        {
            InfoDoctor infoDoctor=new InfoDoctor();
            infoDoctor.setEmail(data.getEmail());
            infoDoctor.setFullname(data.getFullName());
            infoDoctor.setImage_url(data.getImage_url());
            infoDoctor.setId(data.getId());
            infoDoctor.setMakhoa(data.getKhoa().getId());
            infoDoctor.setTenkhoa(data.getKhoa().getTenkhoa());
            infoDoctor.setGender(data.isGender());
            infoDoctor.setSdt(data.getSdt());
            if(data.getDate_of_birth()!=null)
                infoDoctor.setDateofbirth(data.getDate_of_birth().toString());
            else
                infoDoctor.setDateofbirth("");
            infoDoctorList.add(infoDoctor);
        }
        return infoDoctorList;
    }

    @Override
    public InfoDoctor GetDoctorEditId(Long id) {
        Doctor doctor=doctorRepository.findDoctorById(id);
        if(doctor!=null)
        {
            InfoDoctor infoDoctor=new InfoDoctor();
            infoDoctor.setEmail(doctor.getEmail());
            infoDoctor.setFullname(doctor.getFullName());
            infoDoctor.setImage_url(doctor.getImage_url());
            infoDoctor.setId(doctor.getId());
            infoDoctor.setMakhoa(doctor.getKhoa().getId());
            infoDoctor.setTenkhoa(doctor.getKhoa().getTenkhoa());
            infoDoctor.setGender(doctor.isGender());
            infoDoctor.setSdt(doctor.getSdt());
            infoDoctor.setDateofbirth(doctor.getDate_of_birth().toString());
            infoDoctor.setCmnd(doctor.getCmnd());
            infoDoctor.setBangcap(doctor.getBangcap());
            return infoDoctor;
        }
        return null;
    }

    @Override
    public List<InfoDoctor> GetDanhSachDocTorByName(String name) {
        String name_or_email=name.replace(" ","");
        if(name_or_email.length()==0)
        {
            List<Doctor>doctorList= doctorRepository.findDanhSachDoctor();
            List<InfoDoctor>infoDoctorList=new ArrayList<>();
            for(Doctor data:doctorList)
            {
                InfoDoctor infoDoctor=new InfoDoctor();
                infoDoctor.setEmail(data.getEmail());
                infoDoctor.setFullname(data.getFullName());
                infoDoctor.setImage_url(data.getImage_url());
                infoDoctor.setId(data.getId());
                infoDoctor.setMakhoa(data.getKhoa().getId());
                infoDoctor.setTenkhoa(data.getKhoa().getTenkhoa());
                infoDoctor.setGender(data.isGender());
                infoDoctor.setSdt(data.getSdt());
                if(data.getDate_of_birth()!=null)
                    infoDoctor.setDateofbirth(data.getDate_of_birth().toString());
                else
                    infoDoctor.setDateofbirth("");
                infoDoctorList.add(infoDoctor);
            }
            return infoDoctorList;
        }
        else
        {
            List<Doctor>doctorList= doctorRepository.findDanhSachDoctorByName(name_or_email);
            List<InfoDoctor>infoDoctorList=new ArrayList<>();
            for(Doctor data:doctorList)
            {
                InfoDoctor infoDoctor=new InfoDoctor();
                infoDoctor.setEmail(data.getEmail());
                infoDoctor.setFullname(data.getFullName());
                infoDoctor.setImage_url(data.getImage_url());
                infoDoctor.setId(data.getId());
                infoDoctor.setMakhoa(data.getKhoa().getId());
                infoDoctor.setTenkhoa(data.getKhoa().getTenkhoa());
                infoDoctor.setGender(data.isGender());
                infoDoctor.setSdt(data.getSdt());
                if(data.getDate_of_birth()!=null)
                    infoDoctor.setDateofbirth(data.getDate_of_birth().toString());
                else
                    infoDoctor.setDateofbirth("");
                infoDoctorList.add(infoDoctor);
            }
            return infoDoctorList;
        }
    }

    @Override
    public boolean BacSiXinNghiPhep(BacSiXinNghiDTO bacSiXinNghiDTO) {
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(doctor!=null)
        {
            List<LichNghiBacSi>lichNghiBacSiList=lichNghiBacSiRepository.SearchBaseOnEmailAndDate(doctor.getEmail(),dateFormat.ConvertStringToDate(bacSiXinNghiDTO.getDate()));
            for(LichNghiBacSi data:lichNghiBacSiList)
            {
                if(data.getDate_off().equals(bacSiXinNghiDTO.getDate()))
                {
                    return false;
                }
            }
            LichNghiBacSi lichNghiBacSi=new LichNghiBacSi();
            lichNghiBacSi.setDescription(bacSiXinNghiDTO.getDescription());
            lichNghiBacSi.setDate_off(dateFormat.ConvertStringToDate(bacSiXinNghiDTO.getDate()));
            doctor.ThemLichNghi(lichNghiBacSi);
            List<LichHenKham>lichHenKhams=lichKhamRepository.LichSuLichHenKhamByDoctorId(doctor.getId(), true,dateFormat.ConvertStringToDate(bacSiXinNghiDTO.getDate()));
            for(LichHenKham lichHenKham:lichHenKhams)
            {
                List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnBaseOnLichHenKham(lichHenKham.getId());
                if(hoSoBenhAnList.size()==0)
                {
                    String content="";
                    if(lichHenKham.isBuoi())
                    {
                        content="Xin lỗi. Lịch hen của khách vào sáng ngày "+(dateFormat.ConverDateToString(lichHenKham.getDate_created()))+" sẽ bị hủy do bác sĩ có việc bận đột xuất";
                    }
                    else
                        content="Xin lỗi. Lịch hen của khách vào chiều ngày "+(dateFormat.ConverDateToString(lichHenKham.getDate_created()))+" sẽ bị hủy do bác sĩ "+doctor.getFullName() + "có việc bận đột xuất";
                    emailSenderService.sendSimpleMessage(lichHenKham.getPatient().getEmail(),"Lịch hẹn đã bị hủy",content);
                    lichHenKham.setStatus(false);
                    lichKhamRepository.save(lichHenKham);
                }
            }
            lichNghiBacSiRepository.save(lichNghiBacSi);
            return true;

        }
        return false;
    }

    @Override
    public List<DoctorTodayApointmentDTO> GetTodayApointmentDoctor() {
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<DoctorTodayApointmentDTO>doctorTodayApointmentDTOList=new ArrayList<>();
        if(doctor!=null && doctor.isEnable_status())
        {
            Date today=new Date();
            String toDayString=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetApointmentsTodayByDoctor(dateFormat.ConvertStringToDate(toDayString),doctor.getId());
            int sttsang=0;
            int sttchieu=0;
            for(LichHenKham data:lichHenKhamList)
            {
                DoctorTodayApointmentDTO doctorTodayApointmentDTO=new DoctorTodayApointmentDTO();
                HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnByLichHenId(data.getId(),doctor.getId());
                if(hoSoBenhAn!=null)
                    continue;
                if(data.isStatus() &&  data.isBuoi())
                {
                    int hour=sttsang/4;
                    int min=sttsang%4;
                    doctorTodayApointmentDTO.setBuoi(true);
                    doctorTodayApointmentDTO.setNamsinh(dateFormat.ConverDateToStringToFE(data.getPatient().getDate_of_birth()));
                    doctorTodayApointmentDTO.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                    doctorTodayApointmentDTO.setThoigian((8+hour)+" : "+(15*min));
                    doctorTodayApointmentDTO.setId(data.getId());
                    doctorTodayApointmentDTO.setPatientid(data.getPatient().getId());
                    doctorTodayApointmentDTO.setPatientname(data.getPatient().getFullName());
                    doctorTodayApointmentDTOList.add(doctorTodayApointmentDTO);
                    ++sttsang;
                }
                if(data.isStatus() && !data.isBuoi())
                {
                    int hour=sttchieu/4;
                    int min=sttchieu%4;
                    doctorTodayApointmentDTO.setBuoi(false);
                    doctorTodayApointmentDTO.setNamsinh(dateFormat.ConverDateToStringToFE(data.getPatient().getDate_of_birth()));
                    doctorTodayApointmentDTO.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                    doctorTodayApointmentDTO.setThoigian((13+hour)+" : "+(15*min));
                    doctorTodayApointmentDTO.setId(data.getId());
                    doctorTodayApointmentDTO.setPatientid(data.getPatient().getId());
                    doctorTodayApointmentDTO.setPatientname(data.getPatient().getFullName());
                    doctorTodayApointmentDTOList.add(doctorTodayApointmentDTO);
                    ++sttchieu;
                }
            }
            return doctorTodayApointmentDTOList;
        }
        return doctorTodayApointmentDTOList;
    }

    @Override
    public boolean KhamBenhAndChoThuoc(KhamBenhDTO khamBenhDTO,Long lichdatkhamid) {
        HoSoBenhAn hoSoBenhAn=new HoSoBenhAn();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        LichHenKham lichHenKham=lichKhamRepository.getById(lichdatkhamid);
        if(lichHenKham!=null)
        {

            hoSoBenhAn.setDoctor(doctor);
            hoSoBenhAn.setPatient(lichHenKham.getPatient());
            hoSoBenhAn.setChuan_doan(khamBenhDTO.getChuandoan());
            hoSoBenhAn.setLichHenKham(lichHenKham);
            Date today=new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            for(int i=1;i<=khamBenhDTO.getSongaytaikham();i++)
            {
                c.add(Calendar.DATE, 1);
            }
            Date daytaikham=c.getTime();
            String data=dateFormat.ConverDateToString(daytaikham);
            hoSoBenhAn.setNgay_tai_kham(dateFormat.ConvertStringToDate(data));
            hoSoBenhAn.setPrice_kham_benh(khamBenhDTO.getTien_kham());
            hoSoBenhAn.setDate_created(new Date());
            hoSoBenhAnRepository.save(hoSoBenhAn);
            List<HoaDonThuoc>hoaDonThuocListInDb=new ArrayList<>();
            List<ThuocChoKhamBenhDTO>thuocChoKhamBenhDTOList= khamBenhDTO.getThuocChoKhamBenhDTOList();
            for(ThuocChoKhamBenhDTO dataThuocKhamBenh:thuocChoKhamBenhDTOList)
            {
                Thuoc thuoc=thuocRepository.findThuocById(dataThuocKhamBenh.getThuocid());
                if(thuoc.getQuantity()<dataThuocKhamBenh.getQuantity())
                {
                    for(HoaDonThuoc hoaDonThuocInDb:hoaDonThuocListInDb)
                    {
                        hoaDonThuocRepository.delete(hoaDonThuocInDb);
                    }

                    hoSoBenhAnRepository.delete(hoSoBenhAn);
                    return false;
                }
                thuoc.setQuantity(thuoc.getQuantity()- dataThuocKhamBenh.getQuantity());
                HoaDonThuoc hoaDonThuoc=new HoaDonThuoc();
                hoaDonThuoc.setThuoc(thuoc);
                hoaDonThuoc.setQuantity(dataThuocKhamBenh.getQuantity());
                hoaDonThuoc.setPrice_per_quantity(thuoc.getPrice());
                hoSoBenhAn.AddHoaDonThuoc(hoaDonThuoc);
                hoaDonThuocListInDb.add(hoaDonThuoc);
                hoaDonThuocRepository.save(hoaDonThuoc);
            }
            hoSoBenhAnRepository.save(hoSoBenhAn);
            return true;
        }
        return false;
    }

    @Override
    public List<ThuocAvailableDoctorDTO> GetThuocAvailableDoctor() {
        List<Thuoc>thuocList=thuocRepository.GetThuocAvailable();
        List<ThuocAvailableDoctorDTO>thuocAvailableDoctorDTOS=new ArrayList<>();
        for(Thuoc data:thuocList)
        {
            ThuocAvailableDoctorDTO thuocAvailableDoctorDTO=new ThuocAvailableDoctorDTO();
            thuocAvailableDoctorDTO.setId(data.getId());
            thuocAvailableDoctorDTO.setQuantity(data.getQuantity());
            thuocAvailableDoctorDTO.setName(data.getName());
            thuocAvailableDoctorDTO.setPrice(data.getPrice());
            thuocAvailableDoctorDTOS.add(thuocAvailableDoctorDTO);
        }
        return thuocAvailableDoctorDTOS;
    }

    @Override
    public List<ThuocAvailableDoctorDTO> GetThuocAvailableDoctorByName(NameThuocDoctorDTO nameThuocDoctorDTO) {
        List<Thuoc>thuocList=thuocRepository.GetThuocAvailableByName(nameThuocDoctorDTO.getName());
        List<ThuocAvailableDoctorDTO>thuocAvailableDoctorDTOS=new ArrayList<>();
        for(Thuoc data:thuocList)
        {
            ThuocAvailableDoctorDTO thuocAvailableDoctorDTO=new ThuocAvailableDoctorDTO();
            thuocAvailableDoctorDTO.setId(data.getId());
            thuocAvailableDoctorDTO.setQuantity(data.getQuantity());
            thuocAvailableDoctorDTO.setName(data.getName());
            thuocAvailableDoctorDTO.setPrice(data.getPrice());
            thuocAvailableDoctorDTOS.add(thuocAvailableDoctorDTO);
        }
        return thuocAvailableDoctorDTOS;
    }

    @Override
    public List<BenhNhanTungKhamBenhDTO> GetBenhNhanTungKhamBenh() {
        List<BenhNhanTungKhamBenhDTO>benhNhanTungKhamBenhDTOS=new ArrayList<>();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(doctor!=null) {
            List<HoSoBenhAn> hoSoBenhAnList = hoSoBenhAnRepository.findHoSoBenhAnByDoctor(doctor.getId());
            List<Patient>patientList=patientRepository.GetDanhSachBenhNhan();
            for(Patient patient:patientList)
            {
                for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
                {
                    if(hoSoBenhAn.getPatient().getId()==patient.getId())
                    {
                        BenhNhanTungKhamBenhDTO benhNhanTungKhamBenhDTO=new BenhNhanTungKhamBenhDTO();
                        benhNhanTungKhamBenhDTO.setEmail(patient.getEmail());
                        benhNhanTungKhamBenhDTO.setId(patient.getId());
                        if(patient.isGender())
                            benhNhanTungKhamBenhDTO.setGender("Nam");
                        else
                            benhNhanTungKhamBenhDTO.setGender("Nữ");
                        benhNhanTungKhamBenhDTO.setSdt(String.valueOf(patient.getSdt()));
                        benhNhanTungKhamBenhDTO.setStatus(patient.isEnable_status());
                        benhNhanTungKhamBenhDTO.setName(patient.getFullName());
                        benhNhanTungKhamBenhDTOS.add(benhNhanTungKhamBenhDTO);
                        break;
                    }
                }
            }
        }
        return benhNhanTungKhamBenhDTOS;
    }

    @Override
    public List<BenhNhanTungKhamBenhDTO> GetBenhNhanTungKhamBenhTheoTenOrEmail(String name_or_email) {
        List<BenhNhanTungKhamBenhDTO>benhNhanTungKhamBenhDTOS=new ArrayList<>();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(doctor!=null) {
            List<HoSoBenhAn> hoSoBenhAnList = hoSoBenhAnRepository.findHoSoBenhAnByDoctor(doctor.getId());
            List<Patient>patientList=patientRepository.findDanhSachPatientByName(name_or_email);
            for(Patient patient:patientList)
            {
                for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
                {
                    if(hoSoBenhAn.getPatient().getId()==patient.getId())
                    {
                        BenhNhanTungKhamBenhDTO benhNhanTungKhamBenhDTO=new BenhNhanTungKhamBenhDTO();
                        benhNhanTungKhamBenhDTO.setEmail(patient.getEmail());
                        benhNhanTungKhamBenhDTO.setId(patient.getId());
                        if(patient.isGender())
                            benhNhanTungKhamBenhDTO.setGender("Nam");
                        else
                            benhNhanTungKhamBenhDTO.setGender("Nữ");
                        benhNhanTungKhamBenhDTO.setSdt(String.valueOf(patient.getSdt()));
                        benhNhanTungKhamBenhDTO.setStatus(patient.isEnable_status());
                        benhNhanTungKhamBenhDTO.setName(patient.getFullName());
                        benhNhanTungKhamBenhDTOS.add(benhNhanTungKhamBenhDTO);
                        break;
                    }
                }
            }
        }
        return benhNhanTungKhamBenhDTOS;
    }

    @Override
    public List<HoSoBenhAnDTO> GetHoSoBenhAnBaseOnPatientId(Long id) {
        List<HoSoBenhAnDTO>hoSoBenhAnDTOList=new ArrayList<>();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(doctor!=null)
        {
            Patient patient=patientRepository.findPatientById(id);
            if(patient!=null)
            {
                List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findAllHoSoBenhAnBaseOnBacSiIdAndPatientId(doctor.getId(),patient.getId());
                for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
                {
                    HoSoBenhAnDTO hoSoBenhAnDTO=new HoSoBenhAnDTO();

                    hoSoBenhAnDTO.setChuandoan(hoSoBenhAn.getChuan_doan());
                    hoSoBenhAnDTO.setId(hoSoBenhAn.getId());
                    hoSoBenhAnDTO.setGiatienkhambenh(hoSoBenhAn.getPrice_kham_benh());
                    hoSoBenhAnDTO.setNgaykham(dateFormat.ConverDateToStringToFE(hoSoBenhAn.getDate_created()));
                    hoSoBenhAnDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(hoSoBenhAn.getNgay_tai_kham()));

                    hoSoBenhAnDTOList.add(hoSoBenhAnDTO);
                }
                return hoSoBenhAnDTOList;
            }
            return hoSoBenhAnDTOList;
        }
        return hoSoBenhAnDTOList;
    }

    @Override
    public DetailApointmentDTO GetDetailApointment(Long id) {
        HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnById(id);
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(hoSoBenhAn.getDoctor().getId()==doctor.getId())
        {
            DetailApointmentDTO detailApointmentDTO=new DetailApointmentDTO();
            detailApointmentDTO.setNgaykham(dateFormat.ConverDateToString(hoSoBenhAn.getDate_created()));
            detailApointmentDTO.setTienkhambenh(hoSoBenhAn.getPrice_kham_benh());
            List<ThuocInDetailApointmentDTO>thuocInDetailApointmentDTOList=new ArrayList<>();
            List<HoaDonThuoc>hoaDonThuocList= hoaDonThuocRepository.findByHoSoBenhAnId(id);
            float totalPrice=0;
            for(HoaDonThuoc data:hoaDonThuocList)
            {
                ThuocInDetailApointmentDTO thuocInDetailApointmentDTO=new ThuocInDetailApointmentDTO();
                thuocInDetailApointmentDTO.setGiatien(data.getPrice_per_quantity()*data.getQuantity());
                totalPrice+=thuocInDetailApointmentDTO.getGiatien();
                thuocInDetailApointmentDTO.setSoluong(data.getQuantity());
                thuocInDetailApointmentDTO.setPrice_per_quantity(data.getPrice_per_quantity());
                thuocInDetailApointmentDTO.setTenthuoc(data.getThuoc().getName());
                thuocInDetailApointmentDTOList.add(thuocInDetailApointmentDTO);
            }
            detailApointmentDTO.setTongbienlai(totalPrice+hoSoBenhAn.getPrice_kham_benh());
            detailApointmentDTO.setTenbacsi(hoSoBenhAn.getDoctor().getFullName());
            detailApointmentDTO.setDa_thanh_toan(hoSoBenhAn.isThanh_toan());
            detailApointmentDTO.setThuocdetail(thuocInDetailApointmentDTOList);
            return detailApointmentDTO;
        }
        return null;
    }

    @Override
    public DashboardDTO GetDashboard() {
        DashboardDTO dashboardDTO=new DashboardDTO();
        //patient
        List<Patient>patientList=patientRepository.GetDanhSachBenhNhan();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Date today=new Date();
        int thismonth=today.getMonth();
        int thisyear=today.getYear();
        int lastmonth=0;
        int lastyear=0;
        if(thismonth==0)
        {
            lastmonth=11;
            lastyear=thisyear-1;
        }
        else
        {
            lastmonth=thismonth-1;
            lastyear=thisyear;
        }
        int patienttoday=0;
        int patientmonth=0;
        int patientlastmonth=0;


        int appointmenttoday=0;
        int appointmentmonth=0;
        int appointmentlastmonth=0;

        int khamtoday=0;
        int khammonth=0;
        int khamlastmonth=0;

        int earningtoday=0;
        int earningmonth=0;
        int earninglastmonth=0;
        for(Patient patient:patientList)
        {
            //patient
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnhByPatientIdAndDoctorId(patient.getId(), doctor.getId());
            for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
            {
                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear
                        && hoSoBenhAn.getDate_created().getDate()==today.getDate()
                        && hoSoBenhAn.isThanh_toan())
                {
                    patienttoday+=1;
                }

                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear && hoSoBenhAn.isThanh_toan())
                {
                    patientmonth+=1;
                    break;
                }
                if(hoSoBenhAn.getDate_created().getMonth()==lastmonth && hoSoBenhAn.getDate_created().getYear()==lastyear && hoSoBenhAn.isThanh_toan())
                {
                    patientlastmonth+=1;
                    break;
                }
            }
            //apointment
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetAllLichHenKhamAndDoctorId(patient.getId(), doctor.getId());
            for(LichHenKham lichHenKham:lichHenKhamList)
            {
                if(lichHenKham.getDate_created().getMonth()==thismonth && lichHenKham.getDate_created().getYear()==thisyear )
                {
                    appointmentmonth+=1;
                }
                if(lichHenKham.getDate_created().getMonth()==lastmonth && lichHenKham.getDate_created().getYear()==lastyear )
                {
                    appointmentlastmonth+=1;
                }
                if(lichHenKham.getDate_created().getMonth()==thismonth && lichHenKham.getDate_created().getYear()==thisyear
                        && lichHenKham.getDate_created().getDate()==today.getDate())
                {
                    appointmenttoday+=1;
                }
            }
            //kham benh
            for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
            {
                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear
                        && hoSoBenhAn.getDate_created().getDate()==today.getDate()
                        && hoSoBenhAn.isThanh_toan())
                {
                    khamtoday+=1;
                }

                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear && hoSoBenhAn.isThanh_toan())
                {
                    khammonth+=1;
                }
                if(hoSoBenhAn.getDate_created().getMonth()==lastmonth && hoSoBenhAn.getDate_created().getYear()==lastyear && hoSoBenhAn.isThanh_toan())
                {
                    khamlastmonth+=1;
                }
            }
            //earning
            for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
            {
                List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(hoSoBenhAn.getId());
                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear
                        && hoSoBenhAn.getDate_created().getDate()==today.getDate()
                        && hoSoBenhAn.isThanh_toan())
                {
                    for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                    {
                        earningtoday+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                    }
                }

                if(hoSoBenhAn.getDate_created().getMonth()==thismonth && hoSoBenhAn.getDate_created().getYear()==thisyear && hoSoBenhAn.isThanh_toan())
                {
                    for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                    {
                        earningmonth+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                    }
                }
                if(hoSoBenhAn.getDate_created().getMonth()==lastmonth && hoSoBenhAn.getDate_created().getYear()==lastyear && hoSoBenhAn.isThanh_toan())
                {
                    for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                    {
                        earninglastmonth+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                    };
                }
            }
        }
        dashboardDTO.setTodaypatient(patienttoday);
        dashboardDTO.setMonthpatient(patientmonth);
        if(patientlastmonth==0)
            dashboardDTO.setPercent_patient(patientmonth);
        else
            dashboardDTO.setPercent_patient((float) (patientmonth*1.0/patientlastmonth));

        dashboardDTO.setTodayappointment(appointmenttoday);
        dashboardDTO.setMonthappointment(appointmentmonth);
        if(appointmentlastmonth==0)
            dashboardDTO.setPercent_appointment(appointmentmonth);
        else
            dashboardDTO.setPercent_appointment((float) (appointmentmonth*1.0/appointmentlastmonth));

        //khambenh
        dashboardDTO.setTodaykhambenh(khamtoday);
        dashboardDTO.setMonthkhambenh(khammonth);
        if(khamlastmonth==0)
            dashboardDTO.setPercent_khambenh(khammonth);
        else
            dashboardDTO.setPercent_khambenh((float) (khammonth*1.0/khamlastmonth));

        //earning
        dashboardDTO.setEarning(earningtoday);
        dashboardDTO.setMonthearning(earningmonth);
        if(earninglastmonth==0)
            dashboardDTO.setPercent_earning(earningmonth);
        else
            dashboardDTO.setPercent_earning((float) (earningmonth*1.0/earninglastmonth));

        return dashboardDTO;
    }

    @Override
    public List<TopBenhNhanTrongThangDTO> GetTopBenhNhanTrongThang() {
        List<TopBenhNhanTrongThangDTO>topBenhNhanTrongThangDTOS=new ArrayList<>();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Date date=new Date();
        int month=date.getMonth();
        int year=date.getYear();
        List<Patient>patientList=patientRepository.GetDanhSachBenhNhan();
        for(Patient patient:patientList)
        {
            int solantrongthang=0;
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnhByPatientIdAndDoctorId(patient.getId(),doctor.getId());
            for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
            {
                if(hoSoBenhAn.getDate_created().getMonth()==month && hoSoBenhAn.getDate_created().getYear()==year)
                {
                    ++solantrongthang;
                }
            }
            if(solantrongthang!=0)
            {
                TopBenhNhanTrongThangDTO topBenhNhanTrongThangDTO=new TopBenhNhanTrongThangDTO();
                topBenhNhanTrongThangDTO.setEmail(patient.getEmail());
                topBenhNhanTrongThangDTO.setName(patient.getFullName());
                topBenhNhanTrongThangDTO.setId(patient.getId());
                topBenhNhanTrongThangDTO.setSdt(String.valueOf(patient.getSdt()));
                topBenhNhanTrongThangDTO.setSolan(solantrongthang);
                topBenhNhanTrongThangDTOS.add(topBenhNhanTrongThangDTO);
            }
        }
        return topBenhNhanTrongThangDTOS;
    }

    @Override
    public List<TopThuocTrongThangDTO> GetThuocTrongThang() {
        List<TopThuocTrongThangDTO>thuocTrongThangDTOS=new ArrayList<>();
        Doctor doctor= doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Thuoc>thuocList=thuocRepository.GetAllMedicine();
        Date date=new Date();
        int month=date.getMonth();
        int year=date.getYear();
        for(Thuoc thuoc:thuocList)
        {
            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByThuocIdAndDoctorId(thuoc.getId(),doctor.getId());
            int quantity=0;
            for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
            {
                if(hoaDonThuoc.getHosobenhan().getDate_created().getYear()==year &&
                hoaDonThuoc.getHosobenhan().getDate_created().getMonth()==month)
                {
                    quantity+=hoaDonThuoc.getQuantity();
                }
            }
            if(quantity!=0)
            {
                TopThuocTrongThangDTO topThuocTrongThangDTO=new TopThuocTrongThangDTO();
                topThuocTrongThangDTO.setId(thuoc.getId());
                topThuocTrongThangDTO.setQuantity(quantity);
                topThuocTrongThangDTO.setTenthuoc(thuoc.getName());
                thuocTrongThangDTOS.add(topThuocTrongThangDTO);
            }
        }
        return thuocTrongThangDTOS;
    }
}
