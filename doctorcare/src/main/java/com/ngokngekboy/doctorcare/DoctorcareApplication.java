package com.ngokngekboy.doctorcare;

import com.ngokngekboy.doctorcare.jwt.AESUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class DoctorcareApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(DoctorcareApplication.class, args);
		SecretKey key = AESUtil.generateKey(128);
		AESUtil.key=key;
		IvParameterSpec ivParameterSpec = AESUtil.generateIv();
		AESUtil.ivParameterSpec=ivParameterSpec;
	}


}
