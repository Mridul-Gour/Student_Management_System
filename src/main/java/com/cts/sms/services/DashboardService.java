package com.cts.sms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.sms.repository.AttendanceRepository;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.MarksRepository;
import com.cts.sms.repository.StudentRepository;
import com.cts.sms.repository.SubjectRepository;
import com.cts.sms.services.AttendanceService;
import com.cts.sms.model.Course;
import com.cts.sms.model.Student;

@Service
public class DashboardService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;
    
    @Autowired
    private MarksService marksService;
    
    @Autowired
    private MarksRepository marksRepo;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    public long getStudentCount() {
        return studentRepo.count();
    }

    public long getCourseCount() {
        return courseRepo.count();
    }

    public long getSubjectCount() {
        return subjectRepo.count();
    }
    
   

    public List<Student> getRecentStudents() {
        return studentRepo.findTop5ByOrderByStudentIdDesc();
    }
    
    public double getStudentGPA(Long studentId) {
        return marksService.calculateOverallGPA(studentId);
    }
    
    public double getAttendancePercentage(Long studentId) {
        long totalClasses = attendanceRepository.countByStudentId(studentId);
        long presentClasses = attendanceRepository.countByStudentIdAndPresent(studentId, true);
       
        return (totalClasses == 0) ? 0 : (presentClasses * 100.0 / totalClasses);
    }
    public List<Course> getEnrolledCourses(Long studentId) {
        return courseRepo.findCoursesByStudentId(studentId);
    }
    
    public int getEnrolledCoursesCount(Long studentId) {
        return getEnrolledCourses(studentId).size();
    }

    public int getEnrolledSubjectsCount(Long studentId) {
        // fetch courses first
        List<Course> courses = getEnrolledCourses(studentId);

        // flatten subjects from all courses
        return courses.stream()
                      .flatMap(c -> c.getSubjects().stream())
                      .collect(Collectors.toSet()) // avoid duplicates
                      .size();
    }
    
    public double getTodaysAttendancePercentage() {
        return attendanceService.getTodayAttendancePercentage();
    }

    public long getTodaysPresentCount() {
        return attendanceService.getTodayPresentCount();
    }

    public long getTodaysTotalCount() {
        return attendanceService.getTodayTotalCount();
    }

}
