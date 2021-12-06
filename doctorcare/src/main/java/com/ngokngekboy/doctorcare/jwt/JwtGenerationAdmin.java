package com.ngokngekboy.doctorcare.jwt;

import com.ngokngekboy.doctorcare.dao.AdminRepository;
import com.ngokngekboy.doctorcare.entity.Admin;
import com.ngokngekboy.doctorcare.entity.Doctor;
import com.ngokngekboy.doctorcare.entity.Patient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static com.ngokngekboy.doctorcare.jwt.AESUtil.ivParameterSpec;

@Component
public class JwtGenerationAdmin {
    private Environment environment;
    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "example.io";
    private AdminRepository adminRepository;
    @Autowired
    public JwtGenerationAdmin(Environment environment,AdminRepository adminRepository) {
        this.environment = environment;
        this.adminRepository=adminRepository;
    }

    public String tokenJwt(Admin admin) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String key=environment.getProperty("token.secret");
        SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject("Token cho admin")
                .claim("username",admin.getEmail())
                .claim("adminid",admin.getId())
                .claim("role","ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(Long.parseLong(environment.getProperty("token.expirationday")))))
                .signWith(SignatureAlgorithm.HS512, key.getBytes(Charset.forName("UTF-8")))
                .compact();
    }




}
