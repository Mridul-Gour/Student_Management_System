package com.cts.sms.controller;

import com.cts.sms.exception.CourseActionException;
import com.cts.sms.model.Course;
import com.cts.sms.model.Department;
import com.cts.sms.repository.DepartmentRepository;
import com.cts.sms.services.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentRepository departmentRepository; // Use repo directly

    // List all courses
    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "admin-course-list"; // Thymeleaf will handle null department
    }

    // Show Add Course form
    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentRepository.findAll());
        return "add-course";
    }
    // Save new course
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, Model model) {
        if (courseService.isCourseCodeExist(course.getCourseCode())) {
            model.addAttribute("course", course);
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("errorMessage", "Course code already exists!");
            return "add-course"; // return to form with error
        }
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // Show Edit Course form
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id);
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("course", course);
        model.addAttribute("departments", departments);
        return "edit-course";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course, Model model) {
        // Check for duplicate code
        boolean isDuplicate = courseService.getAllCourses().stream()
                .anyMatch(c -> c.getCourseCode().equals(course.getCourseCode()) && !c.getId().equals(id));

        if (isDuplicate) {
            model.addAttribute("course", courseService.findById(id)); // populate full course
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("errorMessage", "Course code already exists!");
            return "edit-course"; 
        }

        try {
            // Attempt update
            courseService.updateCourse(id, course);
            return "redirect:/courses"; 
        } catch (CourseActionException e) {
            model.addAttribute("course", courseService.findById(id)); // populate full course for UI
            model.addAttribute("departments", departmentRepository.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "edit-course"; 
        }
    }



    // Delete course
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, Model model) {
        try {
            courseService.deleteCourse(id);
            return "redirect:/courses"; // success
        } catch (CourseActionException e) {
            // Show friendly error message on the list page
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            return "admin-course-list"; // stay on list page
        }
    }

}
