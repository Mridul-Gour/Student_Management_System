package com.cts.sms.controller;

import com.cts.sms.model.Course;
import com.cts.sms.model.Subject;
import com.cts.sms.services.CourseService;
import com.cts.sms.services.SubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses/subjects")
public class AdminSubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseService courseService;

    // List all subjects
    @GetMapping
    public String listSubjects(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", subjects);
        return "subject-list";
    }

    // Show Add Subject form
    @GetMapping("/new")
    public String showAddSubjectForm(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("courses", courseService.getAllCourses());
        return "add-subject";
    }

    // Save Subject
    @PostMapping("/save")
    public String saveSubject(@ModelAttribute Subject subject) {
        Long courseId = subject.getCourse().getId();
        Course course = courseService.findById(courseId);
        subject.setCourse(course);
        subjectService.saveSubject(subject);
        return "redirect:/courses/subjects";
    }

    // Show Edit form
    @GetMapping("/edit/{id}")
    public String showEditSubjectForm(@PathVariable Long id, Model model) {
        Subject subject = subjectService.getAllSubjects()
                                       .stream()
                                       .filter(s -> s.getId().equals(id))
                                       .findFirst()
                                       .orElseThrow(() -> new RuntimeException("Subject not found"));
        model.addAttribute("subject", subject);
        model.addAttribute("courses", courseService.getAllCourses());
        return "edit-subject";
    }

    // Update Subject
    @PostMapping("/update/{id}")
    public String updateSubject(@PathVariable Long id, @ModelAttribute Subject subject) {
        Long courseId = subject.getCourse().getId();
        Course course = courseService.findById(courseId);
        subject.setCourse(course);
        subject.setId(id);
        subjectService.saveSubject(subject);
        return "redirect:/courses/subjects";
    }

    // Delete Subject
    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/courses/subjects";
    }
}
