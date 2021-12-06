package com.ngokngekboy.doctorcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngokngekboy.doctorcare.common.DateFormat;
import com.ngokngekboy.doctorcare.dao.*;
import com.ngokngekboy.doctorcare.dto.InfoMedicineDTO;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.NameMedicineDTOO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.admin.*;
import com.ngokngekboy.doctorcare.dto.patient.PatientInforDTO;
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
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

}
