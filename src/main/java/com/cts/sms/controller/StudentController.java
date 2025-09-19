package com.cts.sms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.sms.model.Course;
import com.cts.sms.model.Department;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.DepartmentRepository;
import com.cts.sms.repository.SubjectRepository;
import com.cts.sms.services.CourseService;
import com.cts.sms.services.DepartmentService;
import com.cts.sms.services.StudentService;
import com.cts.sms.services.SubjectService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService service;
	
	@Autowired
	private DepartmentService deptService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SubjectService subjectService;
	
	@GetMapping("/byId/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id)
	{
		Student students = service.getStudentById(id);
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@PostMapping("/updating")
	public String updateStudent(@Valid @ModelAttribute("student") Student student,
	                            BindingResult result,
	                            RedirectAttributes redirect,
	                            Model model) {
		
		Student existingStudent = service.getStudentById(student.getStudentId());

	    if(result.hasErrors()) {
	    	
	    	model.addAttribute("department", existingStudent.getDepartment().getName());
            model.addAttribute("courses", existingStudent.getCourses());   
            model.addAttribute("subjects", existingStudent.getSubjects()); 
	        return "update-student";
	    }


	    

	    student.setCourses(existingStudent.getCourses());
	    student.setSubjects(existingStudent.getSubjects());

	    service.updateStudent(student.getStudentId(), student);

	    redirect.addFlashAttribute("message", "Profile updated Successfully !!");
	    return "redirect:/student/profile/" + student.getStudentId();
	}


	
	@GetMapping("/update/{id}")
	public String updatePage(@PathVariable Long id, Model model)
	{
		Student s = service.getStudentById(id);
		
		if (s.getDepartment() == null) {
	        Department dept = new Department();
	        dept.setName("N/A");   
	        s.setDepartment(dept);
	    }

        // Populate IDs for checkboxes
        s.setCourseIds(s.getCourses().stream().map(Course::getId).toList());
        s.setSubjectIds(s.getSubjects().stream().map(Subject::getId).toList());

        model.addAttribute("student", s);
        model.addAttribute("department", s.getDepartment().getName());
        model.addAttribute("courses", s.getCourses());   
        model.addAttribute("subjects", s.getSubjects());
		return "update-student";
	}
	
//	@GetMapping("/byName/{name}")
	@GetMapping("/search")
	public String getStudentByName(@RequestParam("keyword") String keyword, Model model)
	{
		List<Student> students = service.searchStudents(keyword);
		model.addAttribute("students", students);
		model.addAttribute("keyword", keyword);
		return "student";
	}
	
	@GetMapping("/byYear/{year}")
	public ResponseEntity<List<Student>> getStudentByYear(@PathVariable Integer year)
	{
		List<Student> students = service.searchStudentByYear(year);
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@GetMapping("/byDepartmentName/{name}")
	public ResponseEntity<List<Student>> getStudentByDepartmentName(@PathVariable String name)
	{
		List<Student> students = service.searchStudentByDepartment(name);
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@GetMapping("/profile/{id}")
	public String studentProfile(@PathVariable Long id, Model model)
	{
		Student s = service.getStudentById(id);
		model.addAttribute("student", s);
		return "my-profile";
	}
	
}
