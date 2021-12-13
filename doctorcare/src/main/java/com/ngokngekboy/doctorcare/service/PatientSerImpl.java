package com.ngokngekboy.doctorcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngokngekboy.doctorcare.common.DateFormat;
import com.ngokngekboy.doctorcare.dao.*;
import com.ngokngekboy.doctorcare.dto.*;
import com.ngokngekboy.doctorcare.dto.patient.*;
import com.ngokngekboy.doctorcare.entity.*;
import com.ngokngekboy.doctorcare.jwt.JwtGenerationPatient;
import com.ngokngekboy.doctorcare.sendemail.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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
public class PatientSerImpl implements IPatientSer{

    private PatientRepository patientRepository;
    private ObjectMapper objectMapper;

    private JwtGenerationPatient jwtGenerationPatient;

    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    private HoSoBenhAnRepository hoSoBenhAnRepository;
    private DoctorRepository doctorRepository;
    private LichNghiBacSiRepository lichNghiBacSiRepository;
    private DateFormat dateFormat;
    private LichKhamRepository lichKhamRepository;
    private HoaDonThuocRepository hoaDonThuocRepository;


    @Override
    public boolean PatientRegister(PatientRegisterDTO patientRegisterDTO) {
        Patient patient=patientRepository.findByEmail(patientRegisterDTO.getEmail());
        if(patient==null)
        {
            patient=objectMapper.convertValue(patientRegisterDTO,Patient.class);
            patient.setDate_of_birth(dateFormat.ConvertStringToDate(patientRegisterDTO.getDateofbirth()));
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            patient.setEnable_status(true);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    @Override
    public SuccessDTO PatientLogin(LoginDTO loginDTO) {
        Patient patient=patientRepository.findByEmail(loginDTO.getEmail());
        if(patient!=null && patient.isConfirm_email() && patient.isEnable_status())
        {
            if(passwordEncoder.matches(loginDTO.getPassword(),patient.getPassword()))
            {
                SuccessDTO successDTO=new SuccessDTO();
                try {
                    successDTO.setToken(jwtGenerationPatient.tokenJwt(patient));
                    successDTO.setEmail(loginDTO.getEmail());
                    return successDTO;
                } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;

        }
        return null;
    }

    @Override
    public boolean SendEmailConfirm(PatientRegisterDTO patientRegisterDTO) {
        Patient patient=patientRepository.findByEmail(patientRegisterDTO.getEmail());
        if(patient!=null)
        {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            patient.setToken_confirm_email(uuid);
            patientRepository.save(patient);
            String link="http://localhost:4200/api/register/confirm/token/"+uuid;
            emailSenderService.sendSimpleMessage(patient.getEmail(),"xac nhan dang ky tai ngok ngek boy",link);
            return true;
        }
        return false;
    }

    @Override
    public boolean ConfirmEmail(String token) {
        Patient patient=patientRepository.findByToken_confirm_email(token);
        if(patient!=null)
        {
            patient.setToken_confirm_email("");
            patient.setConfirm_email(true);
            patient.setEnable_status(true);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    @Override
    public List<HoSoBenhAn> DanhSachHoSoBenhAn() {
        List<HoSoBenhAn>hoSoBenhAns=new ArrayList<>();
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(patient!=null)
            hoSoBenhAns=hoSoBenhAnRepository.findHoSoBenhAn(patient.getEmail());
        return hoSoBenhAns;
    }

    @Override
    public boolean DatLichKham(DatLichKhamDTO datLichKhamDTO) {
        Doctor doctor=doctorRepository.findDoctorById(datLichKhamDTO.getBacsiid());
        Date today=new Date();
        String todayToString=dateFormat.ConverDateToString(today);
        if(dateFormat.ConvertStringToDate(todayToString).after(dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham())))
            return false;
        if(doctor!=null && doctor.isEnable_status())
        {
            List<LichNghiBacSi> lichNghiBacSiList=lichNghiBacSiRepository.SearchBaseOnEmailAndDate(doctor.getEmail(),dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham()));
            for(LichNghiBacSi data:lichNghiBacSiList)
            {
                if(data.getDate_off().compareTo(dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham()))==0)
                {
                    return false;
                }
            }
            if(true)
            {
                Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
                if(patient==null)
                    return false;
                int checkPatientAddApointment=lichKhamRepository.SearchLichHenKhamDatePatientId(doctor.getId(),dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham()),datLichKhamDTO.isBuoi(),patient.getId());
                if(checkPatientAddApointment!=0)
                    return false;
                int soluotkham=lichKhamRepository.SearchLichHenKham(doctor.getId(),dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham()),datLichKhamDTO.isBuoi());
                if(soluotkham<=11)
                {
                    LichHenKham lichHenKham=new LichHenKham();

                    lichHenKham.setPatient(patient);
                    lichHenKham.setDoctor(doctor);
                    lichHenKham.setDate_created(dateFormat.ConvertStringToDate(datLichKhamDTO.getNgaykham()));
                    lichHenKham.setBuoi(datLichKhamDTO.isBuoi());
                    lichHenKham.setStt(soluotkham+1);
                    lichHenKham.setStatus(true);
                    patient.ThemLichHenKham(lichHenKham);
                    doctor.ThemLichHenKham(lichHenKham);
                    lichKhamRepository.save(lichHenKham);
                    int hour=soluotkham/4;
                    int min=soluotkham%4;
                    String content="";
                    if(!datLichKhamDTO.isBuoi())
                    {

                        content="Bạn đã đặt thành công bác sĩ: "+doctor.getFullName()+ ", chuyên khoa: "+doctor.getKhoa().getTenkhoa()+", vào ngày: "+
                                datLichKhamDTO.getNgaykham() +", với số thứ tự: "+(soluotkham+1)+ ". Thời gian dự tính là: "+ (13+hour)+"PM:"+(15*min)+". Mong bạn đến đúng giờ";
                    }
                    else
                    {
                        content="Bạn đã đặt thành công bác sĩ: "+doctor.getFullName()+ ", chuyên khoa: "+doctor.getKhoa().getTenkhoa()+", vào ngày: "+
                                datLichKhamDTO.getNgaykham() +", với số thứ tự: "+(soluotkham+1)+ ". Thời gian dự tính là: "+ (8+hour)+"PM:"+(15*min)+". Mong bạn đến đúng giờ";
                    }
                    emailSenderService.sendSimpleMessage(patient.getEmail(),"xac nhan dang ky tai ngok ngek boy",content);
                    return true;

                }
                else
                    return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<LichHenKham> LichHenKhamBenhNhan() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<LichHenKham>lichHenKhams=new ArrayList<>();
        if(patient!=null)
        {
            lichHenKhams=lichKhamRepository.LichSuLichHenKham(patient.getId());
        }
        return lichHenKhams;
    }

    @Override
    public boolean EmailLayLaiMatKhau(String email) {
        Patient patient=patientRepository.findByEmail(email);
        if(patient==null)
        {
            return false;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        patient.setToken_confirm_email(uuid);
        String link="http://localhost:4200/forgotpassword/"+uuid;
        emailSenderService.sendSimpleMessage(patient.getEmail(),"Click vào để lấy lại mật khẩu nhé",link);
        patientRepository.save(patient);
        return true;
    }

    @Override
    public boolean ForgotPassword(String token, UpdatePasswordDTO updatePasswordDTO) {
        Patient patient=patientRepository.findByToken_confirm_email(token);
        if(patient!=null)
        {
                patient.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewpass()));
                patient.setToken_confirm_email("");
                patientRepository.save(patient);
                return true;

        }
        return false;
    }

    @Override
    public List<DoctorBaseOnKhoaIdDTO> GetDanhSachBacSiBaseOnKhoaId(Long id) {
        List<DoctorBaseOnKhoaIdDTO>doctorBaseOnKhoaIdDTOList=new ArrayList<>();
        List<Doctor>doctorList=doctorRepository.findDoctorsByOnKhoaId(id,true);
        for(Doctor data:doctorList)
        {
            DoctorBaseOnKhoaIdDTO doctorBaseOnKhoaIdDTO=new DoctorBaseOnKhoaIdDTO();
            doctorBaseOnKhoaIdDTO.setId(data.getId());
            doctorBaseOnKhoaIdDTO.setName(data.getFullName());
            doctorBaseOnKhoaIdDTOList.add(doctorBaseOnKhoaIdDTO);
        }
        return doctorBaseOnKhoaIdDTOList;
    }

    @Override
    public List<LichLamViecPatientAddApointmentDTO> GetLichLamViecBacSiPatientAddApointment(Long bacsiid) {
        Doctor doctor=doctorRepository.findDoctorById(bacsiid);
        List<LichLamViecPatientAddApointmentDTO>lichLamViecPatientAddApointmentDTOS=new ArrayList<>();
        if(doctor!=null)
        {
            Date today=new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.DATE, 1);
            Date nextDay=c.getTime();
            c.add(Calendar.DATE,1);
            Date next2Day=c.getTime();

            String data=dateFormat.ConverDateToString(today);

            List<Date>dateList=new ArrayList<>();
            dateList.add(dateFormat.ConvertStringToDate(data));
            data=dateFormat.ConverDateToString(nextDay);
            dateList.add(dateFormat.ConvertStringToDate(data));
            data=dateFormat.ConverDateToString(next2Day);
            dateList.add(dateFormat.ConvertStringToDate(data));

            return DoGetLichLamViec(dateList,doctor);
        }
        return lichLamViecPatientAddApointmentDTOS;
    }

    @Override
    public List<TodayAppointment> GetTodayAppointment() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();
        if(patient!=null)
        {
            Date today=new Date();
            String dataDate=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.LichSuLichHenKhamTodayByPatientid(patient.getId(),true,dateFormat.ConvertStringToDate(dataDate));
            for(LichHenKham data:lichHenKhamList)
            {
                TodayAppointment todayAppointment=new TodayAppointment();
                todayAppointment.setBacsiid(data.getDoctor().getId());
                todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
                if(data.isBuoi()) {
                    todayAppointment.setBuoi("Sáng");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((8+hour)+"AM : "+(15*min));

                }
                else {
                    todayAppointment.setBuoi("Chiều");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((13+hour)+"PM : "+(15*min));
                }
                todayAppointment.setTenkhoa(data.getDoctor().getKhoa().getTenkhoa());
                todayAppointment.setBacsiname(data.getDoctor().getFullName());
                todayAppointment.setSdt(data.getDoctor().getSdt());
                todayAppointment.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                todayAppointment.setId(data.getId());
                todayAppointment.setImage_url(data.getDoctor().getImage_url());

                todayAppointmentList.add(todayAppointment);
            }
        }
        return todayAppointmentList;
    }

    @Override
    public boolean HuyAppointment(Long id) {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(patient!=null)
        {
            HoSoBenhAn hoSoBenhAn= hoSoBenhAnRepository.findHoSoBenhAnByLichHenId(id,patient.getId());
            if(hoSoBenhAn==null)
            {
                LichHenKham lichHenKham= lichKhamRepository.getById(id);
                lichHenKham.setUser_send_cancel(true);
                lichKhamRepository.save(lichHenKham);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<ListDanhSachBacSiFree3DayDTO> GetDanhSachBacSiFree3Day() {
        Date today=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);
        Date nextDay=c.getTime();
        c.add(Calendar.DATE,1);
        Date next2Day=c.getTime();

        String dataToday=dateFormat.ConverDateToString(today);

        String dateNextday=dateFormat.ConverDateToString(nextDay);

        String dateNext2day=dateFormat.ConverDateToString(next2Day);


        List<Date>dateListTemp=new ArrayList<>();
        dateListTemp.add(dateFormat.ConvertStringToDate(dataToday));
        dateListTemp.add(dateFormat.ConvertStringToDate(dateNextday));
        dateListTemp.add(dateFormat.ConvertStringToDate(dateNext2day));

        List<ListDanhSachBacSiFree3DayDTO>listDanhSachBacSiFree3DayDTOS=new ArrayList<>();
        List<Doctor>doctorList=doctorRepository.findDanhSachDoctor();
        List<LichHenKham>lichHenKhamList=lichKhamRepository.LichSuLichHenByDoctor(true,dateFormat.ConvertStringToDate(dataToday),dateFormat.ConvertStringToDate(dateNext2day));
        List<LichNghiBacSi>lichNghiBacSiList=lichNghiBacSiRepository.LichNghiBacSiBetween2day(dateFormat.ConvertStringToDate(dataToday),dateFormat.ConvertStringToDate(dateNext2day));
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        for(Doctor doctor:doctorList)
        {
            ListDanhSachBacSiFree3DayDTO listDanhSachBacSiFree3DayDTO=new ListDanhSachBacSiFree3DayDTO();
            List<LichHenKham>lichHenKhamsBaseOnBacSi=lichHenKhamList.stream().filter(data->data.getDoctor().getId()==doctor.getId()).collect(Collectors.toList());
            List<LichNghiBacSi>lichNghiBacSiList1=lichNghiBacSiList.stream().filter(data->data.getDoctor().getId()==doctor.getId()).collect(Collectors.toList());
            if(lichNghiBacSiList1.size()==3)
                break;
            List<Date>dateList=dateListTemp;
            for(LichNghiBacSi data:lichNghiBacSiList1)
            {
                dateList=dateList.stream().filter(x->x.compareTo(data.getDate_off())!=0).collect(Collectors.toList());
            }
            List<DanhSachBacSiFree3DayDTO>danhSachBacSiFree3DayDTOS=new ArrayList<>();
            for(Date data:dateList)
            {
                DanhSachBacSiFree3DayDTO danhSachBacSiFree3DayDTO=new DanhSachBacSiFree3DayDTO();
                danhSachBacSiFree3DayDTO.setDay(dateFormat.ConverDateToString(data));
                int sang=0;
                int chieu=0;
                for(LichHenKham dataLichHenKham:lichHenKhamsBaseOnBacSi)
                {
                    if(dataLichHenKham.isBuoi())
                    {
                        ++sang;
                    }
                    else
                    {
                        ++chieu;
                    }
                }
                List<Boolean>session=new ArrayList<>();
                if(sang<=11)
                {
                    Boolean boolSang=new Boolean(true);
                    LichHenKham lichHenKham=lichKhamRepository.SearchLichHenKhamByDateAndPatientIdAndBuoi(patient.getId(),true,data,true);
                    if(lichHenKham==null)
                        session.add(boolSang);
                }

                if(chieu<=11)
                {
                    Boolean boolChieu=new Boolean(false);
                    LichHenKham lichHenKham=lichKhamRepository.SearchLichHenKhamByDateAndPatientIdAndBuoi(patient.getId(),true,data,false);
                    if(lichHenKham==null)
                        session.add(boolChieu);
                }
                danhSachBacSiFree3DayDTO.setSession(session);
                danhSachBacSiFree3DayDTOS.add(danhSachBacSiFree3DayDTO);
            }
            listDanhSachBacSiFree3DayDTO.setDanhSachBacSiFree3DayDTOList(danhSachBacSiFree3DayDTOS);
            listDanhSachBacSiFree3DayDTO.setBacsiid(doctor.getId());
            listDanhSachBacSiFree3DayDTO.setBacsiname(doctor.getFullName());
            listDanhSachBacSiFree3DayDTO.setKhoaid(doctor.getKhoa().getId());
            listDanhSachBacSiFree3DayDTO.setKhoaname(doctor.getKhoa().getTenkhoa());
            listDanhSachBacSiFree3DayDTOS.add(listDanhSachBacSiFree3DayDTO);
        }
        return listDanhSachBacSiFree3DayDTOS;
    }

    @Override
    public List<LichSuApointmentDTO> GetLichSuApointment() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<LichSuApointmentDTO>lichSuApointmentDTOList=new ArrayList<>();
        if(patient!=null)
        {
            List<LichHenKham>lichHenKhamList=lichKhamRepository.HistoryApointment(patient.getId());
            for(LichHenKham dataLichHenKham:lichHenKhamList)
            {
                LichSuApointmentDTO lichSuApointmentDTO=new LichSuApointmentDTO();
                if(!dataLichHenKham.isStatus())
                {
                    lichSuApointmentDTO.setTenbs(dataLichHenKham.getDoctor().getFullName());
                    lichSuApointmentDTO.setId(dataLichHenKham.getId());
                    lichSuApointmentDTO.setDate(dateFormat.ConverDateToString(dataLichHenKham.getDate_created()));
                    lichSuApointmentDTO.setGio("0");
                    lichSuApointmentDTO.setStatus(false);
                    lichSuApointmentDTO.setId(dataLichHenKham.getId());
                    lichSuApointmentDTOList.add(lichSuApointmentDTO);
                    continue;
                }
                HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnhByLichHenId(dataLichHenKham.getId());
                if(hoSoBenhAn!=null)
                    lichSuApointmentDTO.setCheck(true);
                else
                    lichSuApointmentDTO.setCheck(false);
                List<LichHenKham>lichHenKhamList1=lichKhamRepository.GetApointmentsBaseOnDate(dataLichHenKham.getDate_created());
                int realstt=0;

                for(LichHenKham  data:lichHenKhamList1)
                {
                    if(data.isStatus() && dataLichHenKham.isBuoi()==data.isBuoi())
                    {
                        ++realstt;
                    }
                }
                lichSuApointmentDTO.setDate(dateFormat.ConverDateToString(dataLichHenKham.getDate_created()));
                --realstt;
                int hour=realstt/4;
                int min=realstt%4;
                lichSuApointmentDTO.setTenbs(dataLichHenKham.getDoctor().getFullName());
                if(dataLichHenKham.isStatus()) {
                    if (dataLichHenKham.isBuoi())
                        lichSuApointmentDTO.setGio((8 + hour) + ":" + (15 * min));
                    else
                        lichSuApointmentDTO.setGio((13 + hour) + ":" + (15 * min));
                    lichSuApointmentDTO.setStatus(true);

                }
                lichSuApointmentDTO.setId(dataLichHenKham.getId());
                lichSuApointmentDTO.setId(dataLichHenKham.getId());
                lichSuApointmentDTOList.add(lichSuApointmentDTO);

            }
        }
        return lichSuApointmentDTOList;
    }

    @Override
    public DetailApointmentDTO GetDetailApointment(Long id) {
        HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnById(id);
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(hoSoBenhAn.getPatient().getId()==patient.getId())
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
    public DetailPatientDTO GetDetailPatient() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(patient!=null)
        {
            DetailPatientDTO detailPatientDTO=new DetailPatientDTO();
            detailPatientDTO.setCmnd(patient.getCmnd());
            detailPatientDTO.setDiachi(patient.getAddress());
            if(patient.isGender())
            {
                detailPatientDTO.setGioitinh("Nam");
            }
            else
                detailPatientDTO.setGioitinh("Nữ");
            if(patient.getImage_url()==null)
                detailPatientDTO.setImage_url("");
            else
                detailPatientDTO.setImage_url(patient.getImage_url());
            detailPatientDTO.setFullname(patient.getFullName());
            detailPatientDTO.setNgaysinh(dateFormat.ConverDateToString(patient.getDate_of_birth()));
            return detailPatientDTO;
        }
        return null;
    }

    @Override
    public boolean DatLaiApointmentDaHuy(Long id) {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(patient!=null) {
            Date today = new Date();
            String dataDate = dateFormat.ConverDateToString(today);
            LichHenKham lichHenKham = lichKhamRepository.GetLichHenKhamById(id);
            if(lichHenKham.getPatient()==patient)
            {
                lichHenKham.setUser_send_cancel(false);
                lichKhamRepository.save(lichHenKham);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<TodayAppointment> GetApointmentFromToday() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();
        if(patient!=null)
        {
            Date today=new Date();
            String dataDate=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamPatientFromToday(patient.getId(),true,dateFormat.ConvertStringToDate(dataDate));
            for(LichHenKham data:lichHenKhamList)
            {
                TodayAppointment todayAppointment=new TodayAppointment();
                todayAppointment.setBacsiid(data.getDoctor().getId());
                todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
                if(data.isBuoi()) {
                    todayAppointment.setBuoi("Sáng");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((8+hour)+"AM : "+(15*min));

                }
                else {
                    todayAppointment.setBuoi("Chiều");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((13+hour)+"PM : "+(15*min));
                }
                todayAppointment.setTenkhoa(data.getDoctor().getKhoa().getTenkhoa());
                todayAppointment.setBacsiname(data.getDoctor().getFullName());
                todayAppointment.setSdt(data.getDoctor().getSdt());
                todayAppointment.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                todayAppointment.setId(data.getId());
                todayAppointment.setImage_url(data.getDoctor().getImage_url());
                todayAppointmentList.add(todayAppointment);
            }
        }
        return todayAppointmentList;
    }

    @Override
    public List<TodayAppointment> GetApointmentBeforeToday() {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();
        if(patient!=null)
        {
            Date today=new Date();
            String dataDate=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamUserCancel();
            for(LichHenKham data:lichHenKhamList)
            {
                TodayAppointment todayAppointment=new TodayAppointment();
                todayAppointment.setBacsiid(data.getDoctor().getId());
                todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
                if(data.isBuoi()) {
                    todayAppointment.setBuoi("Sáng");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((8+hour)+"AM : "+(15*min));

                }
                else {
                    todayAppointment.setBuoi("Chiều");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    todayAppointment.setThoigian((13+hour)+"PM : "+(15*min));
                }
                HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnhByLichHenId(data.getId());
                if(hoSoBenhAn!=null)
                {
                    todayAppointment.setBs_da_chuandoan(true);
                }
                else
                    todayAppointment.setBs_da_chuandoan(false);
                todayAppointment.setStatus(data.isStatus());
                todayAppointment.setTenkhoa(data.getDoctor().getKhoa().getTenkhoa());
                todayAppointment.setBacsiname(data.getDoctor().getFullName());
                todayAppointment.setSdt(data.getDoctor().getSdt());
                todayAppointment.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                todayAppointment.setId(data.getId());
                todayAppointment.setImage_url(data.getDoctor().getImage_url());
                todayAppointmentList.add(todayAppointment);
            }
        }
        return todayAppointmentList;
    }

    @Override
    public boolean UpdatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(patient!=null)
        {
            if(passwordEncoder.matches(updatePasswordDTO.getOldpass(),patient.getPassword()))
            {
                patient.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewpass()));
                patientRepository.save(patient);
                return true;
            }
            return false;
        }
        return false;
    }


    private List<LichLamViecPatientAddApointmentDTO> DoGetLichLamViec(List<Date> dateList,Doctor doctor)
    {
        List<LichNghiBacSi> lichNghiBacSiList=lichNghiBacSiRepository.SearchBaseOnEmailAndDate(doctor.getEmail(),dateList.get(0));
        List<LichLamViecPatientAddApointmentDTO>lichLamViecPatientAddApointmentDTOS=new ArrayList<>();
        Patient patient=patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        for(Date date:dateList)
        {

            boolean check=true;
            for(LichNghiBacSi lichNghiBacSi:lichNghiBacSiList)
            {
                if(lichNghiBacSi.getDate_off().compareTo(date)==0)
                {
                    check=false;
                    break;
                }
            }
            if(!check)
                break;
            LichLamViecPatientAddApointmentDTO lichLamViecPatientAddApointmentDTO=new LichLamViecPatientAddApointmentDTO();
            List<Boolean>sessionList=new ArrayList<>();
            int buoisang=lichKhamRepository.SearchLichHenKham(doctor.getId(),date,true);
            if(buoisang<=11)
            {
                int lichHenKhamList=lichKhamRepository.SearchLichHenKhamDatePatientId(doctor.getId(),date,true,patient.getId());
                if(lichHenKhamList==0)
                {
                    Boolean sang=new Boolean(true);
                    sessionList.add(sang);
                }
            }
            int buoichieu=lichKhamRepository.SearchLichHenKham(doctor.getId(),date,false);
            if(buoichieu<=11)
            {
                int lichHenKhamList=lichKhamRepository.SearchLichHenKhamDatePatientId(doctor.getId(),date,false,patient.getId());
                if(lichHenKhamList==0) {
                    Boolean chieu = new Boolean(false);
                    sessionList.add(chieu);
                }
            }
            lichLamViecPatientAddApointmentDTO.setDate(dateFormat.ConverDateToString(date));
            lichLamViecPatientAddApointmentDTO.setSession(sessionList);
            lichLamViecPatientAddApointmentDTOS.add(lichLamViecPatientAddApointmentDTO);
        }
        return lichLamViecPatientAddApointmentDTOS;
    }
}
