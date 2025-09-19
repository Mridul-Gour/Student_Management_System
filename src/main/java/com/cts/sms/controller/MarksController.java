package com.cts.sms.controller;

import com.cts.sms.dto.MarksDTO;
import com.cts.sms.services.CourseService;
import com.cts.sms.services.MarksService;
import com.cts.sms.services.StudentService;
import com.cts.sms.services.SubjectService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/marks")
public class MarksController {

    @Autowired
    private MarksService marksService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    // ----- VIEW -----
    @GetMapping("/view")
    public String viewAllMarks(Model model) {
        model.addAttribute("marksList", marksService.getAllMarks());
        model.addAttribute("students", studentService.getAllStudents());
        return "grades";
    }

    // ----- ADD -----
    @GetMapping("/add")
    public String addMarksPage(Model model) {
        model.addAttribute("marks", new MarksDTO());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "marks-form";
    }

    @PostMapping("/add")
    public String addMarks(@Valid @ModelAttribute("marks") MarksDTO marksDTO,
                           BindingResult result,
                           RedirectAttributes redirect,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "marks-form";
        }

        try {
            marksService.addMarks(marksDTO);
            redirect.addFlashAttribute("message", "Marks added successfully!");
            return "redirect:/admin/marks/view";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage()); // dynamic message
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "marks-form";
        }
    }

    // ----- EDIT -----
    @GetMapping("/update/{id}")
    public String editMarksPage(@PathVariable Long id, Model model) {
        MarksDTO marksDTO = marksService.getMarksById(id);
        model.addAttribute("marks", marksDTO);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "marks-form";
    }

    @PostMapping("/update/{id}")
    public String updateMarks(@PathVariable Long id,
                              @Valid @ModelAttribute("marks") MarksDTO marksDTO,
                              BindingResult result,
                              RedirectAttributes redirect,
                              Model model) {

        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "marks-form";
        }

        try {
            marksService.updateMarks(id, marksDTO);
            redirect.addFlashAttribute("message", "Marks updated successfully!");
            return "redirect:/admin/marks/view";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage()); // dynamic message
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "marks-form";
        }
    }

    // ----- DELETE -----
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteMarks(@PathVariable Long id) {
        marksService.deleteMarks(id);
        return "success";
    }

    // ----- JSON Endpoints -----
    @GetMapping(value = "/student/{studentId}/average", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Double> getAverageMarks(@PathVariable Long studentId,
                                                  @RequestParam(value = "semester", required = false) String semester) {
        double avg = (semester != null && !semester.isEmpty())
                ? marksService.calculateAverageMarks(studentId, semester)
                : marksService.calculateAverageMarks(studentId);
        return ResponseEntity.ok(avg);
    }

    @GetMapping(value = "/student/{studentId}/gpa/{semester}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Double> getGPA(@PathVariable Long studentId,
                                         @PathVariable String semester) {
        return ResponseEntity.ok(marksService.calculateGPA(studentId, semester));
    }

    @GetMapping(value = "/student/{studentId}/grade/{semester}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getGrade(@PathVariable Long studentId,
                                           @PathVariable String semester) {
        return ResponseEntity.ok(marksService.calculateGrade(studentId, semester));
    }
    
    
    


    
    
    
    
    
    
    
    
}
