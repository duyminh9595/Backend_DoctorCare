package com.ngokngekboy.doctorcare.controller.patient;

import com.ngokngekboy.doctorcare.dao.PatientRepository;
import com.ngokngekboy.doctorcare.dto.LoginDTO;
import com.ngokngekboy.doctorcare.dto.SuccessDTO;
import com.ngokngekboy.doctorcare.dto.UpdatePasswordDTO;
import com.ngokngekboy.doctorcare.dto.doctor.ForgotPasswordDTO;
import com.ngokngekboy.doctorcare.dto.patient.PatientRegisterDTO;
import com.ngokngekboy.doctorcare.dto.patient.SelfRegisterDTO;
import com.ngokngekboy.doctorcare.service.IPatientSer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/patient")
public class registerlogincontroller {

    private IPatientSer iPatientSer;

    @PostMapping("/register")
    public ResponseEntity Register(@RequestBody PatientRegisterDTO patientRegisterDTO)
    {
        boolean checkRegister=iPatientSer.PatientRegister(patientRegisterDTO);
        if(checkRegister)
        {
            boolean checkSendEmail=iPatientSer.SendEmailConfirm(patientRegisterDTO);
            if(checkSendEmail)
            {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.ok("Gui email that bai");
        }
        return ResponseEntity.ok("Dang Ky That Bai");
    }
    @PostMapping("/selfregister")
    public ResponseEntity SelfRegister(@RequestBody SelfRegisterDTO patientRegisterDTO)
    {
        boolean checkRegister=iPatientSer.PatientSelfRegister(patientRegisterDTO);
        if(checkRegister)
        {
            boolean checkSendEmail=iPatientSer.SendEmailConfirmRegister(patientRegisterDTO);
            if(checkSendEmail)
            {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.ok("Gui email that bai");
        }
        return ResponseEntity.ok("Dang Ky That Bai");
    }

    @PostMapping("/login")
    public ResponseEntity LoginPatient(@RequestBody LoginDTO loginDTO)
    {
        SuccessDTO successDTO=iPatientSer.PatientLogin(loginDTO);
        if(successDTO==null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else
        {

//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.ok(successDTO);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity ConfirmEmailPatient(@RequestParam String tokenemail)
    {
        boolean check=iPatientSer.ConfirmEmail(tokenemail);
        if(check)
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("/maillaylaimatkhau")
    public ResponseEntity GuiMailLayLaiMatKhau(@RequestBody ForgotPasswordDTO forgotPasswordDTO)
    {
        boolean check=iPatientSer.EmailLayLaiMatKhau(forgotPasswordDTO.getEmail());
        if(check)
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @PostMapping("/renewpassword")
    public ResponseEntity PatientRenewPassword(@RequestParam String token, @RequestBody UpdatePasswordDTO updatePasswordDTO)
    {
        boolean check=iPatientSer.ForgotPassword(token,updatePasswordDTO);
        if(check)
        {
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
