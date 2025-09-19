package com.cts.sms.controller;

import com.cts.sms.dto.MarksDTO;
import com.cts.sms.services.MarksService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentMarksController {

    @Autowired
    private MarksService marksService;
    
    @Autowired
    private com.cts.sms.repository.StudentRepository studentRepository;

    @GetMapping("/marks")
    public String viewMarks(Model model, Principal principal) {
        // get logged in student's username/email
        String username = principal.getName();

        // fetch studentId using repository
        Long studentId = studentRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Student not found"))
                .getStudentId();

        List<MarksDTO> marksList = marksService.getMarksByStudent(studentId);
        model.addAttribute("marksList", marksList);
        model.addAttribute("studentId", studentId);
        return "student-marks";
    }


    // REST endpoint to fetch average marks as JSON
    @GetMapping(value = "/marks/average/{semester}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Double> getAverageMarks(@RequestParam Long studentId, @PathVariable String semester) {
        Double avg = marksService.calculateAverageMarks(studentId, semester);
        return ResponseEntity.ok(avg);
    }

    // REST endpoint to fetch GPA for the given semester as JSON
    @GetMapping(value = "/marks/gpa/{semester}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Double> getGpaForSemester(@RequestParam Long studentId, @PathVariable String semester) {
        Double gpa = marksService.calculateGPA(studentId, semester);
        return ResponseEntity.ok(gpa);
    }
    
   
}

