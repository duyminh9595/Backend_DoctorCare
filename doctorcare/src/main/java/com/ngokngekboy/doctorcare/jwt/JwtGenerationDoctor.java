package com.ngokngekboy.doctorcare.jwt;

import com.ngokngekboy.doctorcare.entity.Doctor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
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
@AllArgsConstructor
public class JwtGenerationDoctor {
    private Environment environment;

    public String tokenJwtDoctor(Doctor doctor) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String key=environment.getProperty("token.secret");
        SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject("Token cho admin")
                .claim("username",doctor.getEmail())
                .claim("doctorid",doctor.getId())
                .claim("role","DOCTOR")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(Long.parseLong(environment.getProperty("token.expirationday")))))
                .signWith(SignatureAlgorithm.HS512, key.getBytes(Charset.forName("UTF-8")))
                .compact();

    }
}
