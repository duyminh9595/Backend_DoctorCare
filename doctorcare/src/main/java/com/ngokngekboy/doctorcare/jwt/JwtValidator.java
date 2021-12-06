package com.ngokngekboy.doctorcare.jwt;

import com.ngokngekboy.doctorcare.dao.AdminRepository;
import com.ngokngekboy.doctorcare.dao.DoctorRepository;
import com.ngokngekboy.doctorcare.dao.PatientRepository;
import com.ngokngekboy.doctorcare.entity.Admin;
import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.Patient;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.ngokngekboy.doctorcare.jwt.AESUtil.ivParameterSpec;

public class JwtValidator extends OncePerRequestFilter {
    private Environment environment;
    private PatientRepository patientRepository;
    private AdminRepository adminRepository;
    private DoctorRepository doctorRepository;
    @Autowired
    public JwtValidator(Environment environment, PatientRepository patientRepository,AdminRepository adminRepository,DoctorRepository doctorRepository)
    {
        this.environment=environment;
        this.patientRepository=patientRepository;
        this.adminRepository=adminRepository;
        this.doctorRepository=doctorRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        if(token!=null)
        {
            String key=environment.getProperty("token.secret");
            SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            Claims claims= Jwts.parser().setSigningKey(key.getBytes(Charset.forName("UTF-8")))
                    .parseClaimsJws(token)
                    .getBody();
            String role_name=(String)claims.get("role");
            String email=(String)claims.get("username");
            if(role_name.equals("ADMIN"))
            {
                Admin admin=adminRepository.findByEmail(email);
                String role="ROLE_ADMIN";
                if(admin!=null)
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin.getEmail(),null, AuthorityUtils.commaSeparatedStringToAuthorityList(role)));
            }
            else if(role_name.equals("DOCTOR"))
            {
                Doctor doctor=doctorRepository.findByEmail(email);
                String role="ROLE_DOCTOR";
                if(doctor!=null)
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(doctor.getEmail(),null, AuthorityUtils.commaSeparatedStringToAuthorityList(role)));
            }
            else if (role_name.equals("PATIENT"))
            {
                Patient patient=patientRepository.findByEmail(email);
                String role="ROLE_PATIENT";
                if(patient!=null)
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(patient.getEmail(),null, AuthorityUtils.commaSeparatedStringToAuthorityList(role)));
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}