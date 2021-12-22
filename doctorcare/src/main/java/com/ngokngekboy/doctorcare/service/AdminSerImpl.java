package com.ngokngekboy.doctorcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngokngekboy.doctorcare.common.DateFormat;
import com.ngokngekboy.doctorcare.dao.*;
import com.ngokngekboy.doctorcare.dto.InfoMedicineDTO;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.NameMedicineDTOO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.doctor.InfoDoctor;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;
import com.ngokngekboy.doctorcare.dto.patient.TodayAppointment;
import com.ngokngekboy.doctorcare.entity.*;
import com.ngokngekboy.doctorcare.jwt.JwtGenerationAdmin;
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
public class AdminSerImpl implements IAdminSer{

    private ObjectMapper objectMapper;
    private AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtGenerationAdmin jwtGenerationAdmin;
    private DoctorRepository doctorRepository;
    private DateFormat dateFormat;
    private KhoaRepository khoaRepository;
    private PatientRepository patientRepository;
    private ThuocRepository thuocRepository;
    private NhapKhoRepository nhapKhoRepository;
    private ChiTietNhapKhoRepository chiTietNhapKhoRepository;
    private HistoryUpdatePriceRepository historyUpdatePriceRepository;
    private LichKhamRepository lichKhamRepository;
    private HoSoBenhAnRepository hoSoBenhAnRepository;
    private EmailSenderService emailSenderService;
    private HoaDonThuocRepository hoaDonThuocRepository;

    @Override
    public SuccessDTO AdminLogin(LoginDTO loginDTO) {
        Admin admin= adminRepository.findByEmail(loginDTO.getEmail());
        if(admin!=null)
        {
            if(passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword()))
            {
                SuccessDTO successDTO=new SuccessDTO();
                try {
                    successDTO.setToken(jwtGenerationAdmin.tokenJwt(admin));
                    successDTO.setEmail(admin.getEmail());
                    successDTO.setImage_url(admin.getImage_url());
                    return successDTO;
                } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean AdminRegister(LoginDTO loginDTO) {
        Admin admin=adminRepository.findByEmail(loginDTO.getEmail());
        if(admin==null)
        {
            admin=new Admin();
            admin.setEmail(loginDTO.getEmail());
            admin.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
            admin.setFullName("Day la admin test");
            admin.setStatus(true);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    @Override
    public boolean ThemBacSi(ThemBacSiDTO themBacSiDTO) {
        Doctor doctor= doctorRepository.findByEmail(themBacSiDTO.getEmail());
        Khoa khoa= khoaRepository.getById(themBacSiDTO.getMakhoa());
        if(doctor==null && khoa!=null)
        {
            doctor=new Doctor();
            doctor.setDate_created(new Date());
            doctor.setPassword(passwordEncoder.encode(themBacSiDTO.getPassword()));
            doctor.setEmail(themBacSiDTO.getEmail());
            doctor.setDate_of_birth(dateFormat.ConvertStringToDate(themBacSiDTO.getDateofbirth()));
            doctor.setCmnd(themBacSiDTO.getCmnd());
            doctor.setSdt(themBacSiDTO.getSdt());
            doctor.setImage_url(themBacSiDTO.getImage_url());
            doctor.setFullName(themBacSiDTO.getFullname());
            doctor.setEnable_status(true);
            doctor.setGender(themBacSiDTO.isGender());
            doctor.setBangcap(themBacSiDTO.getBangcap());
            khoa.AddDoctor(doctor);
            doctorRepository.save(doctor);
            return true;
        }
        return false;
    }

    @Override
    public ThemBacSiDTO UpdateBacSi(ThemBacSiDTO themBacSiDTO) {
        Doctor doctor= doctorRepository.findByEmail(themBacSiDTO.getEmail());
        Khoa khoa= khoaRepository.getById(themBacSiDTO.getMakhoa());
        if(doctor!=null && khoa!=null)
        {
            doctor.setDate_created(new Date());
            doctor.setEmail(themBacSiDTO.getEmail());
            doctor.setDate_of_birth(dateFormat.ConvertStringToDate(themBacSiDTO.getDateofbirth()));
            doctor.setCmnd(themBacSiDTO.getCmnd());
            doctor.setSdt(themBacSiDTO.getSdt());
            doctor.setImage_url(themBacSiDTO.getImage_url());
            doctor.setFullName(themBacSiDTO.getFullname());
            doctor.setEnable_status(true);
            doctor.setGender(themBacSiDTO.isGender());
            doctor.setBangcap(themBacSiDTO.getBangcap());
            doctorRepository.save(doctor);
            return themBacSiDTO;
        }
        return null;
    }

    @Override
    public List<PatientInforDTO> GetDanhSachBenhNhan() {
        List<PatientInforDTO>patientInforDTOList=new ArrayList<>();
        List<Patient>patientList= patientRepository.GetDanhSachBenhNhan();
        for(Patient data:patientList)
        {
            PatientInforDTO patientInforDTO=new PatientInforDTO();
            patientInforDTO.setId(data.getId());
            patientInforDTO.setGender(data.isGender());
            patientInforDTO.setEmail(data.getEmail());
            patientInforDTO.setFullName(data.getFullName());
            patientInforDTO.setEnable_status(data.isEnable_status());
            patientInforDTO.setSdt(data.getSdt());
            patientInforDTOList.add(patientInforDTO);
        }
        return patientInforDTOList;
    }

    @Override
    public PatientEditDTO GetInfoPatientEdit(Long id) {
        Patient patient=patientRepository.findPatientById(id);
        if(patient!=null)
        {
            PatientEditDTO patientEditDTO=new PatientEditDTO();
            patientEditDTO.setId(patient.getId());
            patientEditDTO.setGender(patient.isGender());
            patientEditDTO.setEmail(patient.getEmail());
            patientEditDTO.setFullName(patient.getFullName());
            patientEditDTO.setCmnd(patient.getCmnd());
            patientEditDTO.setSdt(patient.getSdt());
            patientEditDTO.setPassword(patient.getPassword());
            patientEditDTO.setAddress(patient.getAddress());
            patientEditDTO.setImage_url(patient.getImage_url());
            patientEditDTO.setDate_of_birth(patient.getDate_of_birth().toString());
            patientEditDTO.setStatus(patient.isEnable_status());
            return patientEditDTO;
        }
        return null;
    }

    @Override
    public boolean UpdateBenhNhanEdit(PatientEditDTO patientEditDTO) {
        Patient patientInDB=patientRepository.findPatientById(patientEditDTO.getId());
        if(patientInDB!=null)
        {
            Patient patient=objectMapper.convertValue(patientEditDTO,Patient.class);
            patient.setPassword(passwordEncoder.encode(patientEditDTO.getPassword()));
            patient.setDate_of_birth(dateFormat.ConvertStringToDate(patientEditDTO.getDate_of_birth()));
            patient.setEnable_status(true);
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    @Override
    public boolean DisableBenhNhan(Long id) {
        Patient patient=patientRepository.findPatientById(id);
        if(patient!=null)
        {
            patient.setEnable_status(!patient.isEnable_status());
            patient.setDate_disable(new Date());
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    @Override
    public List<PatientInforDTO> GetDanhSachBenhNhanByNameOrEmail(String name) {
        List<PatientInforDTO>patientInforDTOList=new ArrayList<>();
        name=name.replace(" ","");
        if(name.length()==0)
        {
            return this.GetDanhSachBenhNhan();
        }
        List<Patient>patientList= patientRepository.findDanhSachPatientByName(name);
        for(Patient data:patientList)
        {
            PatientInforDTO patientInforDTO=objectMapper.convertValue(data,PatientInforDTO.class);
            patientInforDTOList.add(patientInforDTO);
        }
        return patientInforDTOList;
    }

    @Override
    public boolean ThemLoaiThuocMoi(ThemThuocMoiDTO themThuocMoiDTO) {
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            Thuoc thuoc=new Thuoc();
            thuoc.setAdmin(admin);
            thuoc.setName(themThuocMoiDTO.getName());
            thuoc.setStatus(true);
            thuoc.setPrice(themThuocMoiDTO.getPrice());
            thuoc.setQuantity(0l);
            thuoc.setLast_update_quantity(new Date());
            Date date=new Date();
            String dateToString= dateFormat.ConverDateToString(date);
            thuoc.setDate_created(dateFormat.ConvertStringToDate(dateToString));
            thuocRepository.save(thuoc);
            return true;
        }
        return false;
    }

    @Override
    public boolean DisableThuoc(DisableThuocDTO disableThuocDTO) {
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            Thuoc thuoc=thuocRepository.findThuocById(disableThuocDTO.getId());
            if(thuoc!=null)
            {
                thuoc.setStatus(!thuoc.isStatus());
                thuoc.setReason(disableThuocDTO.getReason());
                thuoc.setAdmin_update(admin);
                thuocRepository.save(thuoc);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean AdminNhapKhoThuoc(List<ChiTietNhapKhoThuocDTO> chiTietNhapKhoThuocDTOList) {
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            List<ChiTietNhapKhoThuocDTO>nhapKhoThuocDTOList=chiTietNhapKhoThuocDTOList;
            NhapKhoThuoc nhapKhoThuoc=new NhapKhoThuoc();
            nhapKhoThuoc.setAdmin(admin);
            nhapKhoThuoc.setDate_created(new Date());
            nhapKhoRepository.save(nhapKhoThuoc);
            for(ChiTietNhapKhoThuocDTO data:nhapKhoThuocDTOList)
            {
                ChiTietNhapKho chiTietNhapKho=new ChiTietNhapKho();
                chiTietNhapKho.setPrice(data.getPrice());
                chiTietNhapKho.setQuantity(data.getQuantity());
                Thuoc thuoc=thuocRepository.findThuocById(data.getThuocid());
                thuoc.AddChiTietNhapKho(chiTietNhapKho);
                nhapKhoThuoc.NhapKho(chiTietNhapKho);
                thuoc.setQuantity(thuoc.getQuantity()+data.getQuantity());
                thuoc.setLast_update_quantity(new Date());
                chiTietNhapKhoRepository.save(chiTietNhapKho);
            }
            nhapKhoRepository.save(nhapKhoThuoc);
            return true;
        }
        return false;
    }

    @Override
    public boolean UpdatePriceThuoc(UpdatePriceDTO updatePriceDTO) {
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            Thuoc thuoc=thuocRepository.findThuocById(updatePriceDTO.getId());
            HistoryUpdatePrice historyUpdatePrice=new HistoryUpdatePrice();
            historyUpdatePrice.setThuoc(thuoc);
            historyUpdatePrice.setNew_price(updatePriceDTO.getPrice());
            historyUpdatePrice.setOld_price(thuoc.getPrice());
            historyUpdatePrice.setDate_update_pricenew(new Date());
            historyUpdatePriceRepository.save(historyUpdatePrice);
            thuoc.setPrice(updatePriceDTO.getPrice());
            thuoc.setAdmin_update(admin);
            thuoc.setLast_update_price(new Date());
            thuocRepository.save(thuoc);
            return true;
        }
        return false;
    }

    @Override
    public List<AdminTodayAppointmentDTO> AdminGetTodayAppointment() {
        List<AdminTodayAppointmentDTO> adminTodayAppointmentDTOList =new ArrayList<>();
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            Date today=new Date();
            String dataDate=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.LichHenKhamTodayByAdmin(true,dateFormat.ConvertStringToDate(dataDate));
            for(LichHenKham data:lichHenKhamList)
            {
                AdminTodayAppointmentDTO adminTodayAppointmentDTO =new AdminTodayAppointmentDTO();
                adminTodayAppointmentDTO.setBacsiid(data.getDoctor().getId());
                adminTodayAppointmentDTO.setBacsiname(data.getDoctor().getFullName());


                if(data.isBuoi()) {
                    adminTodayAppointmentDTO.setBuoi("Sáng");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    adminTodayAppointmentDTO.setThoigian((8+hour)+"AM : "+(15*min));

                }
                else {
                    adminTodayAppointmentDTO.setBuoi("Chiều");
                    int hour=(data.getStt()-1)/4;
                    int min=(data.getStt()-1)%4;
                    adminTodayAppointmentDTO.setThoigian((13+hour)+"PM : "+(15*min));
                }

                adminTodayAppointmentDTO.setTenkhoa(data.getDoctor().getKhoa().getTenkhoa());
                adminTodayAppointmentDTO.setKhoaid(data.getDoctor().getKhoa().getId());

                adminTodayAppointmentDTO.setSdtbs(data.getDoctor().getSdt());
                adminTodayAppointmentDTO.setSdtbenhnhan(data.getPatient().getSdt());

                adminTodayAppointmentDTO.setBenhnhanid(data.getPatient().getId());

                adminTodayAppointmentDTO.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                adminTodayAppointmentDTO.setId(data.getId());

                adminTodayAppointmentDTOList.add(adminTodayAppointmentDTO);
            }
            return adminTodayAppointmentDTOList;
        }
        return adminTodayAppointmentDTOList;
    }

    @Override
    public boolean AdminHuyApointment(AdminHuyApointmentDTO adminHuyApointmentDTO) {
        Admin admin= adminRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(admin!=null && admin.isStatus())
        {
            HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findLichHenKhamIdExistInHoSoBenhAn(adminHuyApointmentDTO.getId(),true);
            if(hoSoBenhAn==null)
            {
                LichHenKham lichHenKham= lichKhamRepository.getById(adminHuyApointmentDTO.getId());
                if(lichHenKham!=null)
                {
                    int stt=lichHenKham.getStt();
                    lichHenKham.setStatus(false);
                    lichHenKham.setReason(adminHuyApointmentDTO.getReason());
                    lichKhamRepository.save(lichHenKham);
                    List<LichHenKham>lichHenKhamList=lichKhamRepository.GetApointmentAfterSTTCancel(lichHenKham.getDate_created(),lichHenKham.isBuoi());
                    int finalStt = stt;
                    lichHenKhamList=lichHenKhamList.stream().filter(x-> x.getStt()> finalStt).collect(Collectors.toList());

                    for(LichHenKham data:lichHenKhamList)
                    {
                        int hour=(stt-1)/4;
                        int min=(stt-1)%4;
                        String content="";
                        if(data.isBuoi())
                        {
                            content="Lịch hẹn khám của bạn bị đôn lên thành "+(8+hour)+" : "+(15*min) +" . Mong quý khách đến đúng giờ";
                        }
                        else
                        {
                            content="Lịch hẹn khám của bạn bị đôn lên thành "+(13+hour)+" : "+(15*min) +" . Mong quý khách đến đúng giờ";
                        }
                        ++stt;
                        emailSenderService.sendSimpleMessage(data.getPatient().getEmail(),"Thông báo thay đổi thời gian",content);
                    }
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
    public List<InfoMedicineDTO> GetMedicineByName(NameMedicineDTOO nameMedicineDTOO) {
        List<InfoMedicineDTO>infoMedicineDTOList=new ArrayList<>();
        List<Thuoc>thuocs= thuocRepository.findThuocByName(nameMedicineDTOO.getName());
        for(Thuoc data:thuocs)
        {
            InfoMedicineDTO infoMedicineDTO=new InfoMedicineDTO();
            infoMedicineDTO.setId(data.getId());
            infoMedicineDTO.setPrice(data.getPrice());
            infoMedicineDTO.setStatus(data.isStatus());
            infoMedicineDTO.setName(data.getName());
            infoMedicineDTO.setQuantity(data.getQuantity());
            infoMedicineDTOList.add(infoMedicineDTO);
        }
        return infoMedicineDTOList;
    }

    @Override
    public List<InfoMedicineDTO> GetAllMedicine() {
        List<InfoMedicineDTO>infoMedicineDTOList=new ArrayList<>();
        List<Thuoc>thuocs= thuocRepository.GetAllMedicine();
        for(Thuoc data:thuocs)
        {
            InfoMedicineDTO infoMedicineDTO=new InfoMedicineDTO();
            infoMedicineDTO.setId(data.getId());
            infoMedicineDTO.setPrice(data.getPrice());
            infoMedicineDTO.setStatus(data.isStatus());
            infoMedicineDTO.setName(data.getName());
            infoMedicineDTO.setQuantity(data.getQuantity());
            infoMedicineDTOList.add(infoMedicineDTO);
        }
        return infoMedicineDTOList;
    }

    @Override
    public List<TodayAppointment> GetApointmentFromToday() {
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();

            Date today=new Date();
            String dataDate=dateFormat.ConverDateToString(today);
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamFromTodayBefore(true,dateFormat.ConvertStringToDate(dataDate));
            for(LichHenKham data:lichHenKhamList)
            {
                TodayAppointment todayAppointment=new TodayAppointment();
                todayAppointment.setBacsiid(data.getDoctor().getId());
                todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
                todayAppointment.setPatentid(data.getPatient().getId());
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
                if(hoSoBenhAn==null)
                    todayAppointmentList.add(todayAppointment);
            }

        return todayAppointmentList;
    }

    @Override
    public List<TodayAppointment> GetApointmentUserCancel() {
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();

        Date today=new Date();
        String dataDate=dateFormat.ConverDateToString(today);
        List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamUserHasCancel(true,dateFormat.ConvertStringToDate(dataDate));
        for(LichHenKham data:lichHenKhamList)
        {
            TodayAppointment todayAppointment=new TodayAppointment();
            todayAppointment.setBacsiid(data.getDoctor().getId());
            todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
            todayAppointment.setPatentid(data.getPatient().getId());
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
            todayAppointment.setEmailbn(data.getPatient().getEmail());
            if(hoSoBenhAn==null &&  data.isUser_send_cancel())
                todayAppointmentList.add(todayAppointment);
        }

        return todayAppointmentList;
    }

    @Override
    public boolean AdminAproveApointmentUserCancel(Long id) {
        LichHenKham lichHenKham=lichKhamRepository.GetLichHenKhamById(id);
        if(lichHenKham!=null)
        {
            if(!lichHenKham.isUser_send_cancel())
                return false;
            HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnhByLichHenId(lichHenKham.getId());
            if(hoSoBenhAn==null)
            {
                lichHenKham.setStatus(false);
                lichKhamRepository.save(lichHenKham);
                String buoi="";
                if(lichHenKham.isBuoi())
                {
                    buoi="sáng";
                }
                else
                    buoi="chiều";
                String content="Bạn đã hủy thành công lịch khám bệnh mã số: "+lichHenKham.getId()+" vào ngày "+dateFormat.ConverDateToStringToFE(lichHenKham.getDate_created())+" , buổi: "+buoi;
                emailSenderService.sendSimpleMessage(lichHenKham.getPatient().getEmail(),"hủy thành công lịch hẹn khám",content);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<TodayAppointment> GetApointmentUserCancelBaseOnEmail(String email) {
        List<TodayAppointment>todayAppointmentList=new ArrayList<>();

        Date today=new Date();
        String dataDate=dateFormat.ConverDateToString(today);

            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamUserHasCancelBaseOnEmail(email,true,dateFormat.ConvertStringToDate(dataDate));
            for(LichHenKham data:lichHenKhamList)
            {
                TodayAppointment todayAppointment=new TodayAppointment();
                todayAppointment.setBacsiid(data.getDoctor().getId());
                todayAppointment.setUser_send_cancel(data.isUser_send_cancel());
                todayAppointment.setPatentid(data.getPatient().getId());
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
                todayAppointment.setEmailbn(data.getPatient().getEmail());
                if(hoSoBenhAn==null &&  data.isUser_send_cancel())
                    todayAppointmentList.add(todayAppointment);
            }


        return todayAppointmentList;
    }

    @Override
    public AllInforPatientDTO GetAllAboutPatient(Long id) {
        Patient patient=patientRepository.findPatientById(id);
        if(patient!=null)
        {
            AllInforPatientDTO allInforPatientDTO=new AllInforPatientDTO();

            allInforPatientDTO.setCmnd(patient.getCmnd());
            allInforPatientDTO.setEmail(patient.getEmail());
            allInforPatientDTO.setId(patient.getId());
            allInforPatientDTO.setSdt(String.valueOf(patient.getSdt()));
            allInforPatientDTO.setFullname(patient.getFullName());
            allInforPatientDTO.setDiachi(patient.getAddress());
            if(patient.getImage_url()==null)
                allInforPatientDTO.setImageurl("");
            else
                allInforPatientDTO.setImageurl(patient.getImage_url());
            if(patient.isGender())
                allInforPatientDTO.setGioitinh("Nam");
            else
                allInforPatientDTO.setGioitinh("Nữ");
            allInforPatientDTO.setStatus(patient.isEnable_status());
            allInforPatientDTO.setNgaysinh(dateFormat.ConverDateToStringToFE(patient.getDate_of_birth()));
            List<LichHenKham>lichHenKhamList=lichKhamRepository.GetLichHenKhamUserHasCancelBaseOnPatientId(patient.getId());

            List<LichDatHenPatientDTO>lichDatHenPatientDTOList=new ArrayList<>();

            for(LichHenKham data:lichHenKhamList)
            {
                LichDatHenPatientDTO lichDatHenPatientDTO=new LichDatHenPatientDTO();
                lichDatHenPatientDTO.setId(data.getId());
                if(data.isStatus())
                    lichDatHenPatientDTO.setStatus("Admin đã hủy");
                else
                    lichDatHenPatientDTO.setStatus("");
                if(data.isBuoi())
                    lichDatHenPatientDTO.setBuoi("Sáng");
                else
                    lichDatHenPatientDTO.setBuoi("Chiều");
                if(data.isUser_send_cancel())
                    lichDatHenPatientDTO.setUsercancel("User đã hủy");
                else
                    lichDatHenPatientDTO.setUsercancel("");
                lichDatHenPatientDTO.setNgaydat(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                lichDatHenPatientDTOList.add(lichDatHenPatientDTO);
            }

            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnhByPatientId(patient.getId());

            List<HoSoBenhAnDTO>hoSoBenhAnDTOList=new ArrayList<>();

            for(HoSoBenhAn data:hoSoBenhAnList)
            {
                HoSoBenhAnDTO hoSoBenhAnDTO=new HoSoBenhAnDTO();
                hoSoBenhAnDTO.setId(data.getId());
                hoSoBenhAnDTO.setNgaykham(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                hoSoBenhAnDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(data.getNgay_tai_kham()));
                hoSoBenhAnDTO.setIdbacsi(data.getDoctor().getId());
                hoSoBenhAnDTO.setGiatienkhambenh(String.valueOf(data.getPrice_kham_benh()));
                if(data.isThanh_toan())
                    hoSoBenhAnDTO.setDathanhtoan("Đã thanh toán");
                else
                    hoSoBenhAnDTO.setDathanhtoan("Chưa thanh toán");
                hoSoBenhAnDTO.setTenbacsi(data.getDoctor().getFullName());
                hoSoBenhAnDTO.setChuandoan(data.getChuan_doan());
                hoSoBenhAnDTOList.add(hoSoBenhAnDTO);
            }

            allInforPatientDTO.setLichDatHenPatientDTOS(lichDatHenPatientDTOList);
            allInforPatientDTO.setHoSoBenhAnDTOList(hoSoBenhAnDTOList);
            return allInforPatientDTO;
        }
        return null;
    }

    @Override
    public HoSoBenhAnDTO GetALlAboutHoSoBenhAn(Long id) {
        HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnById(id);
        if(hoSoBenhAn!=null)
        {
            HoSoBenhAnDTO hoSoBenhAnDTO=new HoSoBenhAnDTO();
            hoSoBenhAnDTO.setTenbacsi(hoSoBenhAn.getDoctor().getFullName());
            hoSoBenhAnDTO.setChuandoan(hoSoBenhAnDTO.getChuandoan());
            hoSoBenhAnDTO.setGiatienkhambenh(String.valueOf(hoSoBenhAn.getPrice_kham_benh()));
            hoSoBenhAnDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(hoSoBenhAn.getNgay_tai_kham()));

            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(hoSoBenhAn.getId());
            List<ThuocInHoSoBenhAnDTO>thuocInHoSoBenhAnDTOList=new ArrayList<>();
            float totalpricethuoc=0;
            for(HoaDonThuoc data:hoaDonThuocList)
            {
                ThuocInHoSoBenhAnDTO thuoc=new ThuocInHoSoBenhAnDTO();
                thuoc.setGiatien(data.getPrice_per_quantity());
                thuoc.setTenthuoc(data.getThuoc().getName());
                thuoc.setQuantity(data.getQuantity());
                thuoc.setTongcong(data.getQuantity()*data.getPrice_per_quantity());
                totalpricethuoc+=data.getQuantity()* data.getPrice_per_quantity();
                thuocInHoSoBenhAnDTOList.add(thuoc);
            }
            hoSoBenhAnDTO.setTongcongtienthuoc(String.valueOf(totalpricethuoc));
            hoSoBenhAnDTO.setGiatienphaitra(String.valueOf(totalpricethuoc+hoSoBenhAn.getPrice_kham_benh()));
            hoSoBenhAnDTO.setThuocInHoSoBenhAnDTOList(thuocInHoSoBenhAnDTOList);
            hoSoBenhAnDTO.setChuandoan(hoSoBenhAn.getChuan_doan());
            if(hoSoBenhAn.isThanh_toan())
                hoSoBenhAnDTO.setDathanhtoan("Đã thanh toán");
            else
                hoSoBenhAnDTO.setDathanhtoan("Chưa thanh toán");
            hoSoBenhAnDTO.setNgaykham(dateFormat.ConverDateToStringToFE(hoSoBenhAn.getDate_created()));
            hoSoBenhAnDTO.setId(hoSoBenhAn.getId());
            hoSoBenhAnDTO.setStatus(hoSoBenhAn.isThanh_toan());
            return hoSoBenhAnDTO;
        }
        return null;
    }

    @Override
    public AllAboutDoctorDTO GetAllAboutDoctor(Long id) {
        Doctor doctor=doctorRepository.findDoctorById(id);
        if(doctor!=null)
        {
            AllAboutDoctorDTO allAboutDoctorDTO=new AllAboutDoctorDTO();
            allAboutDoctorDTO.setId(doctor.getId());
            allAboutDoctorDTO.setTenbacsi(doctor.getFullName());
            allAboutDoctorDTO.setTenkhoa(doctor.getKhoa().getTenkhoa());
            allAboutDoctorDTO.setCmnd(doctor.getCmnd());
            allAboutDoctorDTO.setBangcap(doctor.getBangcap());
            allAboutDoctorDTO.setNgaysinh(dateFormat.ConverDateToStringToFE(doctor.getDate_of_birth()));
            allAboutDoctorDTO.setEmail(doctor.getEmail());
            if(doctor.isGender())
                allAboutDoctorDTO.setGioitinh("Nam");
            else
                allAboutDoctorDTO.setGioitinh("Nữ");
            allAboutDoctorDTO.setImage_url(doctor.getImage_url());
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnByDoctor(doctor.getId());
            java.util.Date date= new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            int thisyear=cal.get(Calendar.YEAR);

            int socatrongthang=0;
            int socatrongnam=0;
            for(HoSoBenhAn data:hoSoBenhAnList)
            {
                cal.setTime(data.getDate_created());
                int monthindata = cal.get(Calendar.MONTH);
                int yearindata=cal.get(Calendar.YEAR);
                if(month==monthindata && thisyear==yearindata)
                    ++socatrongthang;
                if(thisyear==yearindata)
                    ++socatrongnam;
            }
            allAboutDoctorDTO.setTongluotkham((long) hoSoBenhAnList.size());
            allAboutDoctorDTO.setTongluotkhamtrongnam((long) socatrongnam);
            allAboutDoctorDTO.setTongluotkhamtrongthang((long)socatrongthang);
            allAboutDoctorDTO.setSdt(String.valueOf(doctor.getSdt()));
            return allAboutDoctorDTO;
        }
        return null;
    }

    @Override
    public List<HoSoBenhAnDTO> GetAllHoSoBenhAnBaseDoctor(Long id) {
        List<HoSoBenhAn>hoSoBenhAnList= hoSoBenhAnRepository.findHoSoBenhAnByDoctor(id);
        List<HoSoBenhAnDTO>hoSoBenhAnDTOList=new ArrayList<>();
        for(HoSoBenhAn data:hoSoBenhAnList)
        {
            HoSoBenhAnDTO hoSoBenhAnDTO=new HoSoBenhAnDTO();
            hoSoBenhAnDTO.setId(data.getId());
            hoSoBenhAnDTO.setNgaykham(dateFormat.ConverDateToStringToFE(data.getDate_created()));
            hoSoBenhAnDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(data.getNgay_tai_kham()));
            hoSoBenhAnDTO.setIdbacsi(data.getDoctor().getId());
            hoSoBenhAnDTO.setGiatienkhambenh(String.valueOf(data.getPrice_kham_benh()));
            if(data.isThanh_toan())
                hoSoBenhAnDTO.setDathanhtoan("Đã thanh toán");
            else
                hoSoBenhAnDTO.setDathanhtoan("Chưa thanh toán");
            hoSoBenhAnDTO.setTenbacsi(data.getDoctor().getFullName());
            hoSoBenhAnDTO.setChuandoan(data.getChuan_doan());
            hoSoBenhAnDTO.setTenbenhnhan(data.getPatient().getFullName());
            hoSoBenhAnDTO.setSdtbenhnhan(String.valueOf(data.getPatient().getSdt()));
            if(data.getLichHenKham().isBuoi())
                hoSoBenhAnDTO.setBuoi("Sáng");
            else
                hoSoBenhAnDTO.setBuoi("Chiều");
            hoSoBenhAnDTOList.add(hoSoBenhAnDTO);
        }
        return hoSoBenhAnDTOList;
    }

    @Override
    public List<HoSoBenhAnDTO> GetAllHoSoBenhAnBaseDoctorDateRange(DateRangeDTO rangeDTO, Long id) {
        List<HoSoBenhAn>hoSoBenhAnList= hoSoBenhAnRepository.findHoSoBenhAnByDoctor(id);
        List<HoSoBenhAnDTO>hoSoBenhAnDTOList=new ArrayList<>();
        for(HoSoBenhAn data:hoSoBenhAnList)
        {
            Date datecompare=dateFormat.ConvertStringToDate(dateFormat.ConverDateToString(data.getDate_created()));
            Date dateend=dateFormat.ConvertStringToDate(rangeDTO.getEnd());
            Date datestart=dateFormat.ConvertStringToDate(rangeDTO.getStart());
            if(datecompare.before(dateend) && datecompare.after(datestart) || datecompare.equals(datestart) || datecompare.equals(dateend)) {
                HoSoBenhAnDTO hoSoBenhAnDTO = new HoSoBenhAnDTO();
                hoSoBenhAnDTO.setId(data.getId());
                hoSoBenhAnDTO.setNgaykham(dateFormat.ConverDateToStringToFE(data.getDate_created()));
                hoSoBenhAnDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(data.getNgay_tai_kham()));
                hoSoBenhAnDTO.setIdbacsi(data.getDoctor().getId());
                hoSoBenhAnDTO.setGiatienkhambenh(String.valueOf(data.getPrice_kham_benh()));
                if (data.isThanh_toan())
                    hoSoBenhAnDTO.setDathanhtoan("Đã thanh toán");
                else
                    hoSoBenhAnDTO.setDathanhtoan("Chưa thanh toán");
                hoSoBenhAnDTO.setTenbacsi(data.getDoctor().getFullName());
                hoSoBenhAnDTO.setChuandoan(data.getChuan_doan());
                hoSoBenhAnDTO.setTenbenhnhan(data.getPatient().getFullName());
                hoSoBenhAnDTO.setSdtbenhnhan(String.valueOf(data.getPatient().getSdt()));
                if (data.getLichHenKham().isBuoi())
                    hoSoBenhAnDTO.setBuoi("Sáng");
                else
                    hoSoBenhAnDTO.setBuoi("Chiều");
                hoSoBenhAnDTOList.add(hoSoBenhAnDTO);
            }

        }
        return hoSoBenhAnDTOList;
    }

    @Override
    public boolean UpdateDoctorNewPass(DoctorNewPassDTO doctorNewPassDTO, Long id) {
        Doctor doctor=doctorRepository.findDoctorById(id);
        if(doctor!=null)
        {
            doctor.setPassword(passwordEncoder.encode(doctorNewPassDTO.getNewpass()));
            doctorRepository.save(doctor);
            return true;
        }
        return false;
    }

    @Override
    public List<HoSoBenhAnChuaTraTienDTO> GetChuaTraTienHoSoBenhAn() {
        List<HoSoBenhAnChuaTraTienDTO>hoSoBenhAnChuaTraTienDTOList=new ArrayList<>();
        List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnChuaTraTien();
        for(HoSoBenhAn data:hoSoBenhAnList)
        {
            HoSoBenhAnChuaTraTienDTO hoSoBenhAnChuaTraTienDTO=new HoSoBenhAnChuaTraTienDTO();
            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
            float total=0;
            for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
            {
                total+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
            }
            hoSoBenhAnChuaTraTienDTO.setId(data.getId());
            hoSoBenhAnChuaTraTienDTO.setTenbacsi(data.getDoctor().getFullName());
            hoSoBenhAnChuaTraTienDTO.setTenbenhnhan(data.getPatient().getFullName());
            hoSoBenhAnChuaTraTienDTO.setTienthuoc(String.valueOf(total));
            hoSoBenhAnChuaTraTienDTO.setTienkhambenh(String.valueOf(data.getPrice_kham_benh()));
            hoSoBenhAnChuaTraTienDTO.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
            hoSoBenhAnChuaTraTienDTO.setTongbienlai(String.valueOf(total+data.getPrice_kham_benh()));
            hoSoBenhAnChuaTraTienDTOList.add(hoSoBenhAnChuaTraTienDTO);
        }
        return hoSoBenhAnChuaTraTienDTOList;
    }

    @Override
    public boolean BenhNhanThanhToanTien(Long id) {
        HoSoBenhAn hoSoBenhAn=hoSoBenhAnRepository.findHoSoBenhAnById(id);
        if(hoSoBenhAn!=null)
        {
            hoSoBenhAn.setThanh_toan(true);
            hoSoBenhAnRepository.save(hoSoBenhAn);
            return true;
        }
        return false;
    }

    @Override
    public List<DoanhThuDTO> GetDoanhThuTheoNgay() {
        List<DoanhThuDTO>doanhThuDTOS=new ArrayList<>();
        Date today=new Date();
        Date todayConvert=dateFormat.ConvertStringToDate(dateFormat.ConverDateToString(today));
        List<Doctor>doctorList=doctorRepository.findAllDoctor();
        for(Doctor dataDoctor:doctorList)
        {
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnByDoctor(dataDoctor.getId());
            if(hoSoBenhAnList.size()>0)
            {
                DoanhThuDTO doanhThuDTO=new DoanhThuDTO();
                int tienthuoc=0;
                int tienkhambenh=0;
                for(HoSoBenhAn data:hoSoBenhAnList)
                {
                    int tthuoc=0;
                    if(data.getDate_created().equals(todayConvert))
                    {
                        List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
                        for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                        {
                            tthuoc+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                        }
                        tienthuoc+=tthuoc;
                        tienkhambenh+=data.getPrice_kham_benh();
                    }
                }
                if(tienthuoc>0)
                {
                    doanhThuDTO.setTenbacsi(dataDoctor.getFullName());
                    doanhThuDTO.setTienkhambenh(tienkhambenh);
                    doanhThuDTO.setTienthuoc(tienthuoc);
                    doanhThuDTO.setBacsiid(dataDoctor.getId());
                    doanhThuDTOS.add(doanhThuDTO);
                }
            }
        }
        return doanhThuDTOS;
    }

    @Override
    public List<DoanhThuDTO> GetDoanhThuTheoThangHienTai() {
        List<DoanhThuDTO>doanhThuDTOS=new ArrayList<>();
        Date today=new Date();
        Date todayConvert=dateFormat.ConvertStringToDate(dateFormat.ConverDateToString(today));
        List<Doctor>doctorList=doctorRepository.findAllDoctor();
        for(Doctor dataDoctor:doctorList)
        {
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnByDoctor(dataDoctor.getId());
            if(hoSoBenhAnList.size()>0)
            {
                DoanhThuDTO doanhThuDTO=new DoanhThuDTO();
                int tienthuoc=0;
                int tienkhambenh=0;
                for(HoSoBenhAn data:hoSoBenhAnList)
                {
                    int tthuoc=0;
                    if(data.getDate_created().getMonth()==todayConvert.getMonth() && data.getDate_created().getYear()==todayConvert.getYear())
                    {
                        List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
                        for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                        {
                            tthuoc+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                        }
                        tienthuoc+=tthuoc;
                        tienkhambenh+=data.getPrice_kham_benh();
                    }
                }
                if(tienthuoc>0)
                {
                    doanhThuDTO.setTenbacsi(dataDoctor.getFullName());
                    doanhThuDTO.setTienkhambenh(tienkhambenh);
                    doanhThuDTO.setTienthuoc(tienthuoc);
                    doanhThuDTO.setBacsiid(dataDoctor.getId());
                    doanhThuDTOS.add(doanhThuDTO);
                }
            }
        }
        return doanhThuDTOS;
    }

    @Override
    public List<DoanhThuDTO> GetDoanhThuTheoNamHienTai() {
        List<DoanhThuDTO>doanhThuDTOS=new ArrayList<>();
        Date today=new Date();
        Date todayConvert=dateFormat.ConvertStringToDate(dateFormat.ConverDateToString(today));
        List<Doctor>doctorList=doctorRepository.findAllDoctor();
        for(Doctor dataDoctor:doctorList)
        {
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnByDoctor(dataDoctor.getId());
            if(hoSoBenhAnList.size()>0)
            {
                DoanhThuDTO doanhThuDTO=new DoanhThuDTO();
                int tienthuoc=0;
                int tienkhambenh=0;
                for(HoSoBenhAn data:hoSoBenhAnList)
                {
                    int tthuoc=0;
                    if(data.getDate_created().getYear()==todayConvert.getYear())
                    {
                        List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
                        for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                        {
                            tthuoc+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                        }
                        tienthuoc+=tthuoc;

                        tienkhambenh+=data.getPrice_kham_benh();
                    }
                }
                if(tienthuoc>0)
                {
                    doanhThuDTO.setTenbacsi(dataDoctor.getFullName());
                    doanhThuDTO.setTienkhambenh(tienkhambenh);
                    doanhThuDTO.setTienthuoc(tienthuoc);
                    doanhThuDTO.setBacsiid(dataDoctor.getId());
                    doanhThuDTOS.add(doanhThuDTO);
                }
            }
        }
        return doanhThuDTOS;
    }

    @Override
    public List<DoanhThuDTO> GetDoanhThuTheoDateRange(DateRangeDTO rangeDTO) {
        List<DoanhThuDTO>doanhThuDTOS=new ArrayList<>();
        Date today=new Date();
        Date startdate=dateFormat.ConvertStringToDate(rangeDTO.getStart());
        Date enddate=dateFormat.ConvertStringToDate(rangeDTO.getEnd());
        List<Doctor>doctorList=doctorRepository.findAllDoctor();
        for(Doctor dataDoctor:doctorList)
        {
            List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnByDoctor(dataDoctor.getId());
            if(hoSoBenhAnList.size()>0)
            {
                DoanhThuDTO doanhThuDTO=new DoanhThuDTO();
                int tienthuoc=0;
                int tienkhambenh=0;
                for(HoSoBenhAn data:hoSoBenhAnList)
                {
                    int tthuoc=0;
                    Date dataDate=dateFormat.ConvertStringToDate(dateFormat.ConverDateToString(data.getDate_created()));
                    if(dataDate.after(startdate) && dataDate.before(enddate) || dataDate.equals(startdate)||dataDate.equals(enddate))
                    {
                        List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
                        for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
                        {
                            tthuoc+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
                        }
                        tienthuoc+=tthuoc;

                        tienkhambenh+=data.getPrice_kham_benh();
                    }
                }
                if(tienthuoc>0)
                {
                    doanhThuDTO.setTenbacsi(dataDoctor.getFullName());
                    doanhThuDTO.setTienkhambenh(tienkhambenh);
                    doanhThuDTO.setTienthuoc(tienthuoc);
                    doanhThuDTO.setBacsiid(dataDoctor.getId());
                    doanhThuDTOS.add(doanhThuDTO);
                }
            }
        }
        return doanhThuDTOS;
    }

    @Override
    public List<HoSoBenhAnChuaTraTienDTO> GetChuaTraTienHoSoBenhAnBaseOnId(Long id) {
        List<HoSoBenhAnChuaTraTienDTO>hoSoBenhAnChuaTraTienDTOList=new ArrayList<>();
        List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findHoSoBenhAnChuaTraTienBaseOnId(id);
        for(HoSoBenhAn data:hoSoBenhAnList)
        {
            HoSoBenhAnChuaTraTienDTO hoSoBenhAnChuaTraTienDTO=new HoSoBenhAnChuaTraTienDTO();
            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
            float total=0;
            for(HoaDonThuoc hoaDonThuoc:hoaDonThuocList)
            {
                total+=hoaDonThuoc.getQuantity()*hoaDonThuoc.getPrice_per_quantity();
            }
            hoSoBenhAnChuaTraTienDTO.setId(data.getId());
            hoSoBenhAnChuaTraTienDTO.setTenbacsi(data.getDoctor().getFullName());
            hoSoBenhAnChuaTraTienDTO.setTenbenhnhan(data.getPatient().getFullName());
            hoSoBenhAnChuaTraTienDTO.setTienthuoc(String.valueOf(total));
            hoSoBenhAnChuaTraTienDTO.setTienkhambenh(String.valueOf(data.getPrice_kham_benh()));
            hoSoBenhAnChuaTraTienDTO.setNgay(dateFormat.ConverDateToStringToFE(data.getDate_created()));
            hoSoBenhAnChuaTraTienDTO.setTongbienlai(String.valueOf(total+data.getPrice_kham_benh()));
            hoSoBenhAnChuaTraTienDTOList.add(hoSoBenhAnChuaTraTienDTO);
        }
        return hoSoBenhAnChuaTraTienDTOList;
    }

    @Override
    public DashboardDTO GetTopDashboard() {
        DashboardDTO dashboardDTO=new DashboardDTO();
        Date today=new Date();
        int month=today.getMonth();
        int year=today.getYear();
        List<LichHenKham>lichHenKhamList=lichKhamRepository.GetAllLichHenKham();
        int lichhenkham=0;
        for(LichHenKham data:lichHenKhamList)
        {
            if(data.getDate_created().getMonth()==month && data.getDate_created().getYear()==year)
            {
                lichhenkham+=1;
            }
        }
        dashboardDTO.setAppointments(lichhenkham);
        List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findAllHoSoBenhAn();
        int hosobenhan=0;
        int earning=0;
        for(HoSoBenhAn data:hoSoBenhAnList)
        {
            if(data.getDate_created().getMonth()==month && data.getDate_created().getYear()==year)
            {
                hosobenhan+=1;
                List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByHoSoBenhAnId(data.getId());
                for(HoaDonThuoc dataHoaDonThuoc:hoaDonThuocList)
                {
                    earning+=dataHoaDonThuoc.getQuantity()* dataHoaDonThuoc.getPrice_per_quantity();
                }
            }
        }
        dashboardDTO.setEarning(earning);
        dashboardDTO.setHosokhambenh(hosobenhan);

        List<Patient>patientList=patientRepository.GetDanhSachBenhNhan();
        int slpatient=0;
        for(Patient dataPatient:patientList)
        {
            List<LichHenKham>lichHenKhamList1=lichKhamRepository.GetAllLichHenKham(dataPatient.getId());
            if(lichHenKhamList1.size()>0)
                slpatient+=1;
        }
        dashboardDTO.setNewpatient(slpatient);
        dashboardDTO.setEarning(earning);
        return dashboardDTO;
    }

    @Override
    public List<BacSiDoanhThuTrongThangDTO> GetTopDoanhThuTrongThang() {
        List<BacSiDoanhThuTrongThangDTO>bacSiDoanhThuTrongThangDTOS=new ArrayList<>();
        List<Doctor>doctorList=doctorRepository.findAllDoctor();
        Date today=new Date();
        int month=today.getMonth();
        int year=today.getYear();
        for(Doctor dataDoctor:doctorList)
        {
            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByDoctorId(dataDoctor.getId());
            if(hoaDonThuocList.size()>0)
            {
                BacSiDoanhThuTrongThangDTO bacSiDoanhThuTrongThangDTO=new BacSiDoanhThuTrongThangDTO();
                float doanhthu=0;
                for(HoaDonThuoc data:hoaDonThuocList)
                {
                    if(data.getHosobenhan().getDate_created().getMonth()==month && data.getHosobenhan().getDate_created().getYear()==year)
                    {
                        doanhthu+=data.getQuantity()*data.getPrice_per_quantity();
                    }
                }
                if(doanhthu!=0)
                {
                    bacSiDoanhThuTrongThangDTO.setDoanhthu(doanhthu);
                    bacSiDoanhThuTrongThangDTO.setId(dataDoctor.getId());
                    bacSiDoanhThuTrongThangDTO.setName(dataDoctor.getFullName());
                    bacSiDoanhThuTrongThangDTO.setTenkhoa(dataDoctor.getKhoa().getTenkhoa());
                    bacSiDoanhThuTrongThangDTOS.add(bacSiDoanhThuTrongThangDTO);
                }
            }
        }
        return bacSiDoanhThuTrongThangDTOS.stream().sorted(Comparator.comparingDouble(BacSiDoanhThuTrongThangDTO::getDoanhthu)).collect(Collectors.toList());
    }

    @Override
    public List<ThuocTrongThangDTO> GetThuocDungTrongThang() {
        List<ThuocTrongThangDTO>thuocTrongThangDTOS=new ArrayList<>();
        List<Thuoc>thuocList=thuocRepository.GetAllMedicine();
        Date today=new Date();
        int month=today.getMonth();
        int year=today.getYear();
        for(Thuoc dataThuoc:thuocList)
        {
            List<HoaDonThuoc>hoaDonThuocList=hoaDonThuocRepository.findByThuocId(dataThuoc.getId());
            int quantity=0;
            float total=0;
            for(HoaDonThuoc data:hoaDonThuocList)
            {
                if(data.getHosobenhan().getDate_created().getMonth()==month && data.getHosobenhan().getDate_created().getYear()==year)
                {
                    quantity+=data.getQuantity();
                    total+=data.getQuantity()*data.getPrice_per_quantity();
                }
            }
            if(quantity!=0)
            {
                ThuocTrongThangDTO thuocTrongThangDTO=new ThuocTrongThangDTO();
                thuocTrongThangDTO.setId(dataThuoc.getId());
                thuocTrongThangDTO.setTenthuoc(dataThuoc.getName());
                thuocTrongThangDTO.setPrice(total);
                thuocTrongThangDTO.setQuantity(Long.valueOf(quantity));
                thuocTrongThangDTOS.add(thuocTrongThangDTO);
            }
        }
        return thuocTrongThangDTOS;
    }

    @Override
    public List<CaBenhTrongNgayDTO> GetCaBenhTrongNgay() {
        List<CaBenhTrongNgayDTO>caBenhTrongNgayDTOS=new ArrayList<>();
        List<HoSoBenhAn>hoSoBenhAnList=hoSoBenhAnRepository.findAllHoSoBenhAn();
        Date date=new Date();
        int month=date.getMonth();
        int year=date.getYear();
        int day=date.getDate();
        for(HoSoBenhAn hoSoBenhAn:hoSoBenhAnList)
        {
            if(hoSoBenhAn.getDate_created().getYear()==year && hoSoBenhAn.getDate_created().getMonth()==month && hoSoBenhAn.getDate_created().getDate()==day)
            {
                CaBenhTrongNgayDTO caBenhTrongNgayDTO=new CaBenhTrongNgayDTO();
                caBenhTrongNgayDTO.setNgaytaikham(dateFormat.ConverDateToStringToFE(hoSoBenhAn.getNgay_tai_kham()));
                caBenhTrongNgayDTO.setChuandoan(hoSoBenhAn.getChuan_doan());
                caBenhTrongNgayDTO.setId(hoSoBenhAn.getId());
                caBenhTrongNgayDTO.setTenbacsi(hoSoBenhAn.getDoctor().getFullName());
                caBenhTrongNgayDTO.setTenbenhnhan(hoSoBenhAn.getPatient().getFullName());
                caBenhTrongNgayDTOS.add(caBenhTrongNgayDTO);
            }
        }
        return caBenhTrongNgayDTOS;
    }

    @Override
    public List<InfoDoctor> GetDanhSachDocTorBaseKhoa(Long id) {
        List<Doctor>doctorList= doctorRepository.findDanhSachDoctor();
        List<InfoDoctor>infoDoctorList=new ArrayList<>();
        for(Doctor data:doctorList)
        {
            if(data.getKhoa().getId()==id)
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

        }
        return infoDoctorList;
    }

    @Override
    public List<InfoDoctor> GetDanhSachDocTorByName(String name,Long id) {
        String name_or_email=name.replace(" ","");
        if(name_or_email.length()==0)
        {
            List<Doctor>doctorList= doctorRepository.findDanhSachDoctor();
            List<InfoDoctor>infoDoctorList=new ArrayList<>();
            for(Doctor data:doctorList)
            {
                if(data.getKhoa().getId()==id)
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
            }
            return infoDoctorList;
        }
        else
        {
            List<Doctor>doctorList= doctorRepository.findDanhSachDoctorByName(name_or_email);
            List<InfoDoctor>infoDoctorList=new ArrayList<>();
            for(Doctor data:doctorList)
            {
                if(data.getKhoa().getId()==id)
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
                infoDoctorList.add(infoDoctor);}
            }
            return infoDoctorList;
        }
    }

    @Override
    public boolean DeleteDoctor(Long id) {
        Doctor doctor=doctorRepository.findDoctorById(id);
        if(doctor!=null)
        {
            doctor.setEnable_status(!doctor.isEnable_status());
            System.out.printf(doctor.toString());
            doctorRepository.save(doctor);
            return true;
        }
        return false;
    }


}
