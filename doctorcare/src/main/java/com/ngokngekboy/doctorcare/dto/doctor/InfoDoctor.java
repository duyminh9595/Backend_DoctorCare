package com.ngokngekboy.doctorcare.dto.doctor;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InfoDoctor {
    private Long id;
    @NotNull
    private boolean gender;
    private String image_url;
    @NotNull
    private Long makhoa;
    private String tenkhoa;
    @NotNull
    private String fullname;
    @NotNull
    private String email;
    @NotNull
    private int sdt;
    @NotNull
    private String dateofbirth;
    @NotNull
    private String bangcap;
    @NotNull
    private String cmnd;
    @NotNull
    private boolean status;
}
