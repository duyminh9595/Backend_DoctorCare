package com.ngokngekboy.doctorcare.jwt;

import com.ngokngekboy.doctorcare.dao.PatientRepository;
import com.ngokngekboy.doctorcare.entity.Patient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Date;

import static com.ngokngekboy.doctorcare.jwt.AESUtil.ivParameterSpec;

@Component
public class JwtGenerationPatient {

    private Environment environment;
    private PatientRepository patientRepository;
    @Autowired
    public JwtGenerationPatient(Environment environment,PatientRepository patientRepository) {
        this.environment = environment;
        this.patientRepository=patientRepository;
    }

    public String tokenJwt(Patient patient) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if(patient.isConfirm_email())
        {
            String key=environment.getProperty("token.secret");
            SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            return Jwts.builder()
                    .setSubject("Token cho admin")
                    .claim("username",patient.getEmail())
                    .claim("patentid",patient.getId())
                    .claim("role","PATIENT")
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(Long.parseLong(environment.getProperty("token.expirationday")))))
                    .signWith(SignatureAlgorithm.HS512, key.getBytes(Charset.forName("UTF-8")))
                    .compact();
        }
        else
            return null;

    }
}
