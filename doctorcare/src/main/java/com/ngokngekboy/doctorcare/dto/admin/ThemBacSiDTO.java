package com.ngokngekboy.doctorcare.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ThemBacSiDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    private boolean gender;
    @NotNull
    private Long makhoa;
    @NotNull
    private String fullname;
    @NotNull
    private String dateofbirth;
    @NotNull
    private String cmnd;
    @NotNull
    private int sdt;
    @NotNull
    private String image_url;
    @NotNull
    private String password;
    @NotNull
    private String bangcap;

}
