package com.cts.sms.services;

import com.cts.sms.model.Attendance;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    Attendance markAttendance(Long studentId, Long subjectId, boolean present);
    List<Attendance> getAttendanceByStudent(Long studentId);
    double calculateAttendancePercentage(Long studentId, LocalDate start, LocalDate end);
    List<Attendance> getAllAttendance();
    String generateMonthlyReport(Long studentId, int year, int month);
    List<Attendance> getAbsentees(Long subjectId, LocalDate date);
    
    void markOrUpdateAttendance(Long studentId, Long subjectId, LocalDate date, boolean present, boolean isUpdate);
    long getTodayTotalCount();
    long getTodayPresentCount();
    double getTodayAttendancePercentage();
    
    List<Subject> getSubjectsByCourse(Long courseId);
    List<Student> getStudentsByCourse(Long courseId);
    List<Student> getStudentsForCourseAndSubject(Long courseId, Long subjectId);
    List<Attendance> getAttendanceBySubject(Long subjectId);
    List<Attendance> getAttendanceByDate(LocalDate date);

    
}
    
    
    

