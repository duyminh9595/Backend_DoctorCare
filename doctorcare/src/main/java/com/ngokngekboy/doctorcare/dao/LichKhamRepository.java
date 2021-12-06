package com.ngokngekboy.doctorcare.dao;

import com.ngokngekboy.doctorcare.entity.LichHenKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "lichkham",collectionResourceRel = "lichkham")
@CrossOrigin
public interface LichKhamRepository extends JpaRepository<LichHenKham, Long> {
    @Query("select count(p) from LichHenKham p where p.doctor.id=?1 and p.date_created=?2 and p.buoi=?3 and p.status=true ")
    int SearchLichHenKham(Long id, Date date,boolean buoi);

    @Query("select count(p) from LichHenKham p where p.doctor.id=?1 and p.date_created=?2 and p.buoi=?3 and p.status=true and p.patient.id=?4")
    int SearchLichHenKhamDatePatientId(Long id, Date date,boolean buoi,Long patientid);

    @Query("select p from LichHenKham p where p.patient.id=?1 order by p.date_created")
    List<LichHenKham> LichSuLichHenKham(Long id);

    @Query("select p from LichHenKham p where p.doctor.id=?1 and p.status=?2 and p.date_created=?3 order by p.date_created")
    List<LichHenKham> LichSuLichHenKhamByDoctorId(Long doctorid,boolean status,Date date);

    @Query("select p from LichHenKham p where p.patient.id=?1 and p.status=?2 and p.date_created=?3 order by p.date_created")
    List<LichHenKham> LichSuLichHenKhamTodayByPatientid(Long patientid,boolean status,Date date);

    @Query("select p from LichHenKham p where  p.status=?1 and p.date_created=?2 order by p.date_created")
    List<LichHenKham> LichHenKhamTodayByAdmin(boolean status,Date date);

    @Query("select p from LichHenKham p where p.status=?1 and  p.date_created BETWEEN ?2 AND ?3")
    List<LichHenKham> LichSuLichHenByDoctor(boolean status, Date startDate, Date endDate);

    @Query("select p from LichHenKham p where p.patient.id=?1 and p.status=?2 and p.date_created=?3 and p.buoi=?4 ")
    LichHenKham SearchLichHenKhamByDateAndPatientIdAndBuoi(Long patientid,boolean status,Date date,boolean buoi);

    @Query("select p from LichHenKham p where p.patient.id=?1 order by p.date_created desc")
    List<LichHenKham>HistoryApointment(Long patentid);

    @Query("select p from LichHenKham p where p.date_created=?1")
    List<LichHenKham>GetApointmentsBaseOnDate(Date date);

    @Query("select p from LichHenKham p where p.date_created=?1 and p.doctor.id=?2")
    List<LichHenKham>GetApointmentsTodayByDoctor(Date date,Long doctorid);

    @Query("select p from LichHenKham p where p.date_created=?1 and p.buoi=?2 and p.status=true")
    List<LichHenKham>GetApointmentAfterSTTCancel(Date date,boolean buoi);

}
