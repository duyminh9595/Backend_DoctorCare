package com.ngokngekboy.doctorcare.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "khoa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Khoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten_khoa")
    private String tenkhoa;

    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "khoa")
    private Set<Doctor> doctorSet ;

    public void AddDoctor(Doctor doctor)
    {
        if(doctor!=null)
        {
            if(doctorSet==null)
            {
                doctorSet=new HashSet<Doctor>();
            }
            doctorSet.add(doctor);
            doctor.setKhoa(this);
        }
    }
}
