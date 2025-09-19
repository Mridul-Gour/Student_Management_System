package com.cts.sms.controller;


import com.cts.sms.model.Student;
import com.cts.sms.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentCommonController {

    @Autowired
    private StudentService studentService;

    // View enrolled courses
    @GetMapping("/student/courses")
    public String viewEnrolledCourses(Authentication authentication, Model model) {
        String email = authentication.getName(); // logged-in user email
        Student student = studentService.getStudentByEmail(email);

        if (student == null) {
            model.addAttribute("errorMessage", "Student profile not found for email: " + email);
            return "error";
        }

        model.addAttribute("student", student);
        model.addAttribute("courses", student.getCourses());
        return "student-course-view";
    }

    // View enrolled subjects
    @GetMapping("/student/subjects")
    public String viewEnrolledSubjects(Authentication authentication, Model model) {
        String email = authentication.getName(); // logged-in user email
        Student student = studentService.getStudentByEmail(email);

        if (student == null) {
            model.addAttribute("errorMessage", "Student profile not found for email: " + email);
            return "error";
        }

        model.addAttribute("student", student);
        model.addAttribute("subjects", student.getSubjects());
        return "student-subject-view";
    }
}
