package com.cts.sms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.sms.dto.UpdateMarksForm;
import com.cts.sms.model.Course;
import com.cts.sms.model.Department;
import com.cts.sms.model.Marks;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.repository.StudentRepository;
import com.cts.sms.services.CourseService;
import com.cts.sms.services.DepartmentService;
import com.cts.sms.services.MarksService;
import com.cts.sms.services.StudentService;
import com.cts.sms.services.SubjectService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/student")
public class AdminStudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private MarksService marksService;



    // ------------------- Existing Methods -------------------

    @GetMapping("/add")
    public String addPage(Model model) {
        Student s = new Student();
        s.setCourseIds(new ArrayList<>());
        s.setSubjectIds(new ArrayList<>());

        model.addAttribute("student", s);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "add";
    }

    @PostMapping("/register")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirect) {
    	
    	if (service.isEmailExist(student.getEmail())) {
            result.rejectValue("email", "error.student", "Email already exists!");
        }

        if (service.isPhoneExist(student.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "error.student", "Phone number already exists!");
        }

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "add";
        }        


        if (student.getDepartmentId() != null) {
            student.setDepartment(departmentService.getDepartmentById(student.getDepartmentId()));
        }

        service.saveStudent(student);
        redirect.addFlashAttribute("message", "Student added successfully !!");
        return "redirect:/admin/student/all";
    }

    @GetMapping("/all")
    public String getAllStudents(Model model) {
        List<Student> students = service.getAllStudents();
        model.addAttribute("students", students);
        return "student";
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = service.getStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirect) {
        service.deleteStudent(id);
        redirect.addFlashAttribute("message", "Student Deleted Successfully !!");
        return "redirect:/admin/student/all";
    }

    @PostMapping("/updating")
    public String updateStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                RedirectAttributes redirect,
                                Model model) {
    	
    	if (service.isEmailExistForOther(student.getStudentId(), student.getEmail())) {
            result.rejectValue("email", "error.student", "Email already exists!");
        }

        if (service.isPhoneExistForOther(student.getStudentId(), student.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "error.student", "Phone number already exists!");
        }

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "add";
        }
        

        if (student.getDepartmentId() != null) {
            student.setDepartment(departmentService.getDepartmentById(student.getDepartmentId()));
        }

        Long id = student.getStudentId();
        service.updateStudent(id, student);
        redirect.addFlashAttribute("message", "Student updated Successfully !!");
        return "redirect:/admin/student/all";
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        Student s = service.getStudentById(id);

        s.setCourseIds(s.getCourses().stream().map(Course::getId).toList());
        s.setSubjectIds(s.getSubjects().stream().map(Subject::getId).toList());
        if (s.getDepartment() != null) {
            s.setDepartmentId(s.getDepartment().getDepartmentId());
        }

        model.addAttribute("student", s);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "add";
    }

    @GetMapping("/search")
    public String getStudentByName(@RequestParam("keyword") String keyword, Model model) {
        List<Student> students = service.searchStudents(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "student";
    }

    @GetMapping("/byYear/{year}")
    public ResponseEntity<List<Student>> getStudentByYear(@PathVariable Integer year) {
        List<Student> students = service.searchStudentByYear(year);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/byDepartmentName/{name}")
    public ResponseEntity<List<Student>> getStudentByDepartmentName(@PathVariable String name) {
        List<Student> students = service.searchStudentByDepartment(name);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public String studentProfile(@PathVariable Long id, Model model) {
        Student s = service.getStudentById(id);
        model.addAttribute("student", s);
        return "student_profile";
    }

    @GetMapping("/courses/{departmentId}")
    @ResponseBody
    public List<Course> getCoursesByDepartment(@PathVariable Long departmentId) {
        return courseService.getByDepartmentId(departmentId);
    }

    @GetMapping("/subjects/{courseId}")
    @ResponseBody
    public List<Subject> getSubjectsByCourse(@PathVariable Long courseId) {
        return subjectService.getSubjectsByCourse(courseId);
    }

    // ------------------- New Update Marks Feature -------------------
    
    
 // Inside AdminStudentController

 // Step 1: Show form to select Student ID
 @GetMapping("/update-marks")
 public String showUpdateForm(Model model) {
     model.addAttribute("studentIds", studentRepository.findAll()
             .stream()
             .map(s -> s.getStudentId())
             .toList());
     model.addAttribute("marksList", null);
     model.addAttribute("selectedStudent", null);
     return "update-marks";
 }

 // Step 2: Populate marks for selected student
 @PostMapping("/update-marks")
 public String processUpdateMarksForm(@RequestParam Long studentId, Model model) {
	    Student student = service.getStudentById(studentId);
	    if (student == null) {
	        model.addAttribute("error", "Student not found!");
	        model.addAttribute("studentIds", studentRepository.findAll()
	                .stream()
	                .map(Student::getStudentId)
	                .toList());
	        return "update-marks";
	    }

	    // Fetch all marks for this student
	    List<Marks> marksList = marksService.getMarksByStudentId(studentId);

	    // Populate subject names
	    marksList.forEach(mark -> {
	        Subject subject = subjectService.getSubjectById(mark.getSubjectId());
	        if (subject != null) {
	            mark.setSubjectName(subject.getSubjectName());
	        } else {
	            mark.setSubjectName("N/A");
	        }
	    });

	    model.addAttribute("marksList", marksList);
	    model.addAttribute("studentIds", studentRepository.findAll()
	            .stream()
	            .map(Student::getStudentId)
	            .toList());
	    model.addAttribute("selectedStudent", student);

	    return "update-marks";
	}

	 // Step 3: Show edit page for specific mark
	 @GetMapping("/update-marks/edit/{marksId}")
	 public String editMarks(@PathVariable Long marksId, Model model) {
	     Marks marks = marksService.getMarksEntityById(marksId);
	     if (marks == null) {
	         return "redirect:/admin/student/update-marks";
	     }
	
	     UpdateMarksForm form = new UpdateMarksForm();
	     form.setMarksId(marks.getMarksId());
	     form.setStudentId(marks.getStudentId());
	     form.setMarksObtained(marks.getMarksObtained());
	     form.setExamType(marks.getExamType());
	     form.setSemester(marks.getSemester());
	
	     model.addAttribute("updateMarksForm", form);
	     return "edit-marks";
	 }

	 // Step 4: Save updated marks
	 @PostMapping("/update-marks/save")
	 public String saveUpdatedMarks(@Valid @ModelAttribute("updateMarksForm") UpdateMarksForm form,
	                                BindingResult result,
	                                RedirectAttributes redirect,
	                                Model model) {
	     if (result.hasErrors()) {
	         model.addAttribute("updateMarksForm", form);
	         return "edit-marks";
	     }
	
	     Marks marks = marksService.getMarksEntityById(form.getMarksId());
	     if (marks == null) {
	         redirect.addFlashAttribute("error", "Marks record not found!");
	         return "redirect:/admin/student/update-marks";
	     }
	
	     marks.setMarksObtained(form.getMarksObtained());
	     marks.setExamType(form.getExamType());
	     marks.setSemester(form.getSemester());
	     marksService.saveMarks(marks);
	
	     redirect.addFlashAttribute("message", "Marks updated successfully!");
	     return "redirect:/admin/student/update-marks";
	 }

}
