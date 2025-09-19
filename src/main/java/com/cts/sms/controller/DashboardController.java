package com.cts.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.sms.dto.UserResponseDTO;
import com.cts.sms.services.DashboardService;
import com.cts.sms.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model, HttpSession session) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Get user details from UserService
        UserResponseDTO user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // user name to the model
        model.addAttribute("loggedInUser", user.getName());
        
        // Add other attributes
        model.addAttribute("studentCount", dashboardService.getStudentCount());
        model.addAttribute("courseCount", dashboardService.getCourseCount());
        model.addAttribute("subjectCount", dashboardService.getSubjectCount());
        model.addAttribute("recentStudents", dashboardService.getRecentStudents());
        
        // Add today's attendance attributes
        model.addAttribute("todaysAttendance", dashboardService.getTodaysAttendancePercentage());
        model.addAttribute("todaysPresent", dashboardService.getTodaysPresentCount());
        model.addAttribute("todaysTotal", dashboardService.getTodaysTotalCount());

        
        return "admin-dashboard";
    }
    
    @GetMapping("/student/dashboard")
    public String showStudentDashboard(Model model, HttpSession session) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDTO user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("studentName", user.getName());

        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            throw new RuntimeException("Student ID not found in session. Ensure it is set at login.");
        }

        model.addAttribute("studentId", studentId);
        model.addAttribute("gpa", dashboardService.getStudentGPA(studentId));
        model.addAttribute("attendance", dashboardService.getAttendancePercentage(studentId));
        model.addAttribute("coursesCount", dashboardService.getEnrolledCoursesCount(studentId));
        model.addAttribute("subjectsCount", dashboardService.getEnrolledSubjectsCount(studentId));

        return "student-dashboard";
    }

}
