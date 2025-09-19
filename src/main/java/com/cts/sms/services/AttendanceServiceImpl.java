package com.cts.sms.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.sms.model.Attendance;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.repository.AttendanceRepository;
import com.cts.sms.repository.StudentRepository;


@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentRepository studentRepository;

    
    @Autowired
    private SubjectService subjectService;

    @Override
    public Attendance markAttendance(Long studentId, Long subjectId, boolean present) {
        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setSubjectId(subjectId);
        attendance.setDate(LocalDate.now());
        attendance.setPresent(present);
        return attendanceRepository.save(attendance);
      
    }

    @Override
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    	 
    }

    @Override
    public double calculateAttendancePercentage(Long studentId, LocalDate start, LocalDate end) {
        List<Attendance> records = attendanceRepository.findByStudentIdAndDateBetween(studentId, start, end);
        long total = records.size();
        long present = records.stream().filter(Attendance::getPresent).count();
        return total > 0 ? (present * 100.0 / total) : 0.0;
    	
    }
    @Override
    public String generateMonthlyReport(Long studentId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> records =
                attendanceRepository.findByStudentIdAndDateBetween(studentId, start, end);

        long total = records.size();
        long present = records.stream().filter(Attendance::getPresent).count();

        return String.format("Month: %d-%d, Total Classes: %d, Present: %d, Percentage: %.2f%%",
                month, year, total, present, total > 0 ? (present * 100.0 / total) : 0.0);
    }

    @Override
    public List<Attendance> getAbsentees(Long subjectId, LocalDate date) {
        return attendanceRepository.findAll().stream()
                .filter(a -> a.getSubjectId().equals(subjectId)
                          && a.getDate().equals(date)
                          && !a.getPresent())
                .toList();
    }
    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
   
    @Override
    public void markOrUpdateAttendance(Long studentId, Long subjectId, LocalDate date, boolean present, boolean isUpdate) {
        Attendance record = attendanceRepository.findByStudentIdAndSubjectIdAndDate(studentId, subjectId, date)
                .orElse(new Attendance());
        record.setStudentId(studentId);
        record.setSubjectId(subjectId);
        record.setDate(date);
        record.setPresent(present);
        attendanceRepository.save(record);
    }


    // ----Dashboard Today Attendance-----
    @Override
    public long getTodayTotalCount() {
        LocalDate today = LocalDate.now();
        return attendanceRepository.countByDate(today);
    }

    @Override
    public long getTodayPresentCount() {
        LocalDate today = LocalDate.now();
        return attendanceRepository.countByDateAndPresentTrue(today);
    }

    @Override
    public double getTodayAttendancePercentage() {
        long total = getTodayTotalCount();
        long present = getTodayPresentCount();
        return total > 0 ? (present * 100.0 / total) : 0.0;
    }

    @Override
    public List<Subject> getSubjectsByCourse(Long courseId) {
        return subjectService.getAllSubjects().stream()
                .filter(s -> s.getCourse() != null 
                          && s.getCourse().getId().equals(courseId)) // ✅ use getId() (not getCourseId)
                .toList();
    }
    @Override
    public List<Student> getStudentsForCourseAndSubject(Long courseId, Long subjectId) {
        // fetch all students enrolled in this subject
        List<Student> subjectStudents = studentService.getStudentsBySubject(subjectId);

        // ✅ safety: if you still want to check they belong to the course
        if (courseId != null) {
            return subjectStudents.stream()
                    .filter(s -> s.getCourses().stream()
                            .anyMatch(c -> c.getId().equals(courseId)))
                    .toList();
        }

        return subjectStudents;
    }
    @Override
    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getCourses().stream().anyMatch(c -> c.getId().equals(courseId)))
                .toList();
    }
    @Override
    public List<Attendance> getAttendanceBySubject(Long subjectId) {
        return attendanceRepository.findBySubjectId(subjectId);
    }
    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
}
