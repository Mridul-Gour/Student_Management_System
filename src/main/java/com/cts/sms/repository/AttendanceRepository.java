package com.cts.sms.repository;

import com.cts.sms.model.Attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findBySubjectId(Long subjectId);

    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);
    List<Attendance> findBySubjectIdAndDate(Long subjectId, LocalDate date);
    List<Attendance> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    List<Attendance> findByDate(LocalDate date);
    
    long countByStudentId(Long studentId);
    long countByDate(LocalDate date);
    long countByDateAndPresentTrue(LocalDate date);

    Optional<Attendance> findByStudentIdAndSubjectIdAndDate(Long studentId, Long subjectId, LocalDate date);
    long countByStudentIdAndPresent(Long studentId, Boolean present);
    
    
}

