package com.cts.sms.controller;

import com.cts.sms.model.Attendance;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.services.AttendanceService;
import com.cts.sms.services.CourseService;
import com.cts.sms.services.StudentService;
import com.cts.sms.services.SubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private CourseService courseService;

    // -------------------- Admin Pages ------------------------

    @GetMapping("/admin/dashboard/attendance/mark-attendance")
    public String markAttendancePage(@RequestParam(required = false) Long courseId,
                                     @RequestParam(required = false) Long subjectId,
                                     Model model) {

        model.addAttribute("courses", courseService.getAllCourses());

        if (courseId != null) {
            model.addAttribute("subjects", subjectService.getSubjectsByCourse(courseId));
            model.addAttribute("selectedCourseId", courseId);
        }

        if (subjectId != null) {
            model.addAttribute("students", studentService.getStudentsBySubject(subjectId));
            model.addAttribute("selectedSubjectId", subjectId);
        }

        return "mark-attendance";
    }


    @PostMapping("/admin/dashboard/attendance/mark-attendance")
    public String markAttendanceForAll(@RequestParam Long courseId,
                                       @RequestParam Long subjectId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                       @RequestParam Map<String,String> allParams,
                                       RedirectAttributes redirectAttributes) {

        // Load students enrolled in this subject only
        List<Student> students = studentService.getStudentsBySubject(subjectId);

        for (Student student : students) {
            String presentParam = allParams.get("present_" + student.getStudentId());
            boolean present = presentParam != null && presentParam.equals("on");
            attendanceService.markOrUpdateAttendance(student.getStudentId(), subjectId, date, present, false);
        }

        redirectAttributes.addFlashAttribute("successMessage", "✅ Attendance marked successfully!");
        return "redirect:/admin/dashboard/attendance/mark-attendance?courseId=" + courseId + "&subjectId=" + subjectId;
    }


    @GetMapping("/admin/dashboard/attendance/update-attendance")
    public String updateAttendanceForm(@RequestParam(required = false) Long courseId,
                                       @RequestParam(required = false) Long subjectId,
                                       @RequestParam(required = false) Long studentId,
                                       Model model) {
        model.addAttribute("courses", courseService.getAllCourses());

        if (courseId != null) {
            model.addAttribute("subjects", subjectService.getSubjectsByCourse(courseId));
            model.addAttribute("selectedCourseId", courseId);
        }

        if (courseId != null && subjectId != null) {
            // first fetch students by subject
            List<Student> subjectStudents = studentService.getStudentsBySubject(subjectId);

            // filter students by selected course
            List<Student> filtered = new ArrayList<>();
            for (Student s : subjectStudents) {
                if (s.getCourses() != null && s.getCourses().stream()
                        .anyMatch(c -> c.getId().equals(courseId))) {
                    filtered.add(s);
                }
            }

            model.addAttribute("students", filtered);
            model.addAttribute("selectedSubjectId", subjectId);
        }

        if (studentId != null) {
            model.addAttribute("selectedStudentId", studentId);
        }

        return "update-attendance";
    }


    @PostMapping("/admin/dashboard/attendance/update-attendance")
    public String updateAttendance(@RequestParam Long courseId,
                                   @RequestParam Long studentId,
                                   @RequestParam Long subjectId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(required = false) Boolean present,
                                   RedirectAttributes redirectAttributes) {

        boolean isPresent = present != null && present;
        attendanceService.markOrUpdateAttendance(studentId, subjectId, date, isPresent, true);

        redirectAttributes.addFlashAttribute("successMessage", "✅ Attendance updated successfully!");
        return "redirect:/admin/dashboard/attendance/update-attendance?courseId=" + courseId +
                "&subjectId=" + subjectId + "&studentId=" + studentId;
    }

    // -------------------- View Attendance ------------------------

    @GetMapping("/student/dashboard/attendance/view-attendance")
    public String viewAttendanceStudent(Principal principal, Model model) {
        String email = principal.getName();
        Student student = studentService.getAllStudents().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (student == null) {
            model.addAttribute("attendanceList", null);
            model.addAttribute("error", "Student not found for email: " + email);
            model.addAttribute("role", "student");
            return "view-attendance";
        }

        List<Attendance> records = attendanceService.getAttendanceByStudent(student.getStudentId());

        for (Attendance record : records) {
            Subject subj = subjectService.getAllSubjects().stream()
                    .filter(s -> s.getId().equals(record.getSubjectId()))
                    .findFirst()
                    .orElse(null);

            if (subj != null) {
                record.setSubjectName(subj.getSubjectName());
            }
            record.setStudentName(student.getFirstName() + " " + student.getLastName());
        }

        long totalClasses = records.size();
        long presentClasses = records.stream().filter(Attendance::getPresent).count();

        model.addAttribute("attendanceList", records);
        model.addAttribute("studentName", student.getFirstName() + " " + student.getLastName());
        model.addAttribute("role", "student");
        model.addAttribute("totalClasses", totalClasses);
        model.addAttribute("presentClasses", presentClasses);

        return "view-attendance";
    }

    @GetMapping("/admin/dashboard/attendance/view-attendance")
    public String viewAttendanceAdmin(@RequestParam(required = false) Long studentId,
                                      @RequestParam(required = false) Long toggleStudentId,
                                      @RequestParam(required = false) Long toggleSubjectId,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toggleDate,
                                      @RequestParam(required = false) Boolean togglePresent,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

        model.addAttribute("role", "admin");
        model.addAttribute("students", studentService.getAllStudents());

        // ✅ Handle toggle first and redirect immediately
        if (toggleStudentId != null && toggleSubjectId != null && toggleDate != null && togglePresent != null) {
            attendanceService.markOrUpdateAttendance(toggleStudentId, toggleSubjectId, toggleDate, togglePresent, true);

            redirectAttributes.addFlashAttribute("successMessage", "✅ Attendance updated successfully!");
            return "redirect:/admin/dashboard/attendance/view-attendance?studentId=" + (studentId != null ? studentId : 0);
        }

        List<Attendance> records = null;

        if (studentId == null) {
            // nothing selected yet
        } else if (studentId == 0) {
            // all students
            records = attendanceService.getAllAttendance();
            for (Attendance record : records) {
                Student stu = studentService.getAllStudents().stream()
                        .filter(s -> s.getStudentId().equals(record.getStudentId()))
                        .findFirst().orElse(null);
                if (stu != null) record.setStudentName(stu.getFirstName() + " " + stu.getLastName());

                Subject subj = subjectService.getAllSubjects().stream()
                        .filter(s -> s.getId().equals(record.getSubjectId()))
                        .findFirst().orElse(null);
                if (subj != null) record.setSubjectName(subj.getSubjectName());
            }
        } else {
            // single student
            records = attendanceService.getAttendanceByStudent(studentId);
            Student stu = studentService.getAllStudents().stream()
                    .filter(s -> s.getStudentId().equals(studentId))
                    .findFirst().orElse(null);
            for (Attendance record : records) {
                Subject subj = subjectService.getAllSubjects().stream()
                        .filter(s -> s.getId().equals(record.getSubjectId()))
                        .findFirst().orElse(null);
                if (subj != null) record.setSubjectName(subj.getSubjectName());
                if (stu != null) record.setStudentName(stu.getFirstName() + " " + stu.getLastName());
            }
        }

        model.addAttribute("attendanceList", records);
        return "view-attendance";
    }
    
    @GetMapping("/admin/dashboard/attendance/attendance-management")
    public String attendanceManagement(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long toggleStudentId,
            @RequestParam(required = false) Long toggleSubjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toggleDate,
            @RequestParam(required = false) Boolean togglePresent,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("students", studentService.getAllStudents());

        // ✅ Handle inline toggle
        if (toggleStudentId != null && toggleSubjectId != null && toggleDate != null && togglePresent != null) {
            attendanceService.markOrUpdateAttendance(toggleStudentId, toggleSubjectId, toggleDate, togglePresent, true);
            redirectAttributes.addFlashAttribute("successMessage", "✅ Attendance updated successfully!");

            // stay on same page with selected studentId
            return "redirect:/admin/dashboard/attendance/attendance-management" +
                    (studentId != null ? "?studentId=" + studentId : "");
        }

        // ✅ Fetch attendance list based on selected student
        List<Attendance> attendanceList;
        if (studentId != null) {
            attendanceList = attendanceService.getAttendanceByStudent(studentId);
        } else {
            attendanceList = attendanceService.getAllAttendance();
        }

        // ✅ Fill transient fields for display
        for (Attendance record : attendanceList) {
            Student stu = studentService.getAllStudents().stream()
                    .filter(s -> s.getStudentId().equals(record.getStudentId()))
                    .findFirst().orElse(null);
            if (stu != null) record.setStudentName(stu.getFirstName() + " " + stu.getLastName());

            Subject subj = subjectService.getAllSubjects().stream()
                    .filter(s -> s.getId().equals(record.getSubjectId()))
                    .findFirst().orElse(null);
            if (subj != null) record.setSubjectName(subj.getSubjectName());
        }

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("selectedStudentId", studentId);

        return "attendance-management";
    }


    @GetMapping("/admin/dashboard/attendance/todays-attendance")
    public String todaysAttendance(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   Model model) {

        LocalDate selectedDate = (date != null) ? date : LocalDate.now();
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(selectedDate);

        // Filter only present records
        attendanceList.removeIf(a -> !a.getPresent());

        // Fill transient fields for display
        for (Attendance record : attendanceList) {
            Student stu = studentService.getAllStudents().stream()
                    .filter(s -> s.getStudentId().equals(record.getStudentId()))
                    .findFirst().orElse(null);
            if (stu != null) record.setStudentName(stu.getFirstName() + " " + stu.getLastName());

            Subject subj = subjectService.getAllSubjects().stream()
                    .filter(s -> s.getId().equals(record.getSubjectId()))
                    .findFirst().orElse(null);
            if (subj != null) record.setSubjectName(subj.getSubjectName());
        }

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("selectedDate", selectedDate);

        return "todays-attendance";
    }

}