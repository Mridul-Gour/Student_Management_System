package com.cts.sms.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.sms.exception.StudentNotFoundException;
import com.cts.sms.model.Course;
import com.cts.sms.model.Department;
import com.cts.sms.model.Marks;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.DepartmentRepository;
import com.cts.sms.repository.MarksRepository;
import com.cts.sms.repository.StudentRepository;
import com.cts.sms.repository.SubjectRepository;


@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private DepartmentRepository departmentRepo;

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private MarksRepository marksRepository;
	
	@Override
	public Student saveStudent(Student student) {
	    // Convert courseIds to actual Course entities
	    if (student.getCourseIds() != null && !student.getCourseIds().isEmpty()) {
	        List<Course> courses = courseRepository.findAllById(student.getCourseIds());
	        student.setCourses(courses);
	    } else {
	        student.setCourses(new ArrayList<>()); // initialize empty if none selected
	    }

	    // Convert subjectIds to actual Subject entities
	    if (student.getSubjectIds() != null && !student.getSubjectIds().isEmpty()) {
	        List<Subject> subjects = subjectRepository.findAllById(student.getSubjectIds());
	        student.setSubjects(subjects);
	    } else {
	        student.setSubjects(new ArrayList<>()); // initialize empty if none selected
	    }

	    return studentRepository.save(student);
	}

	@Override
	public Student updateStudent(Long id, Student updatedStudent) {
	    Student existingStudent = studentRepository.findById(id)
	            .orElseThrow(() -> new StudentNotFoundException("Student not found !!"));

	    // Update basic fields
	    existingStudent.setFirstName(updatedStudent.getFirstName());
	    existingStudent.setLastName(updatedStudent.getLastName());
	    existingStudent.setGender(updatedStudent.getGender());
	    existingStudent.setDob(updatedStudent.getDob());
	    existingStudent.setEmail(updatedStudent.getEmail());
	    existingStudent.setAddress(updatedStudent.getAddress());
	    existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
	    existingStudent.setYear(updatedStudent.getYear());

	    // Update department
	    if (updatedStudent.getDepartment() != null && updatedStudent.getDepartment().getDepartmentId() != null) {
	        Department dept = departmentRepo.findById(updatedStudent.getDepartment().getDepartmentId())
	                .orElseThrow(() -> new RuntimeException("Department not found"));
	        existingStudent.setDepartment(dept);
	    }

	    // Update courses
	    if (updatedStudent.getCourseIds() != null) {
	        if (!updatedStudent.getCourseIds().isEmpty()) {
	            List<Course> courses = courseRepository.findAllById(updatedStudent.getCourseIds());
	            existingStudent.setCourses(courses);
	        } else {
	            existingStudent.setCourses(new ArrayList<>()); 
	        }
	    }

	    // Update subjects
	    if (updatedStudent.getSubjectIds() != null) {
	        if (!updatedStudent.getSubjectIds().isEmpty()) {
	            List<Subject> subjects = subjectRepository.findAllById(updatedStudent.getSubjectIds());
	            existingStudent.setSubjects(subjects);
	        } else {
	            existingStudent.setSubjects(new ArrayList<>());
	        }
	    }
	    return studentRepository.save(existingStudent);
	}


	@Override
	public void deleteStudent(Long id) {
		// TODO Auto-generated method stub
		Student deleteStudent = getStudentById(id);
		studentRepository.delete(deleteStudent);
	}

	@Override
	public Student getStudentById(Long id) throws StudentNotFoundException{
		// TODO Auto-generated method stub
		return studentRepository
				.findById(id)
				.orElseThrow(() -> new StudentNotFoundException("Student not found with id : "+id));
	}

	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		return studentRepository.findAll();
	}

	@Override
	public List<Student> searchStudentByName(String name) {
		// TODO Auto-generated method stub
		return studentRepository.findByFirstNameOrLastName(name);
	}

	@Override
	public List<Student> searchStudentByDepartment(String department) {
		// TODO Auto-generated method stub
		return studentRepository.findByDepartment_NameContainingIgnoreCase(department);
	}

	@Override
	public List<Student> searchStudentByYear(Integer year) {
		// TODO Auto-generated method stub
		return studentRepository.findByYear(year);
	}
	
	@Override
	public List<Student> searchStudents(String keyword)
	{
		if(keyword == null || keyword.trim().isEmpty())
		{
			return studentRepository.findAll();
		}
		
		Integer year = null;
	    try {
	        year = Integer.parseInt(keyword);
	    } catch (NumberFormatException e) {
	        // keyword is not a number, ignoring year search
	    }
	    
		return studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrYear(keyword, keyword, keyword, keyword, year);
	}
	
	
	public Student getStudentByEmail(String email) {
	    return studentRepository.findByEmail(email).orElse(null);
	}
	


	@Override
    public Long getStudentIdByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(Student::getStudentId)
                .orElseThrow(() -> new RuntimeException("Student not found for email: " + email));
    }
	
	@Override
    public List<Student> getStudentsByCourse(Long courseId) {
        return studentRepository.findByCourses_Id(courseId);  
    }
	
	@Override
	public List<Student> getStudentsBySubject(Long subjectId) {
	    return studentRepository.findStudentsBySubject(subjectId);
	}

	@Override
	public boolean isEmailExist(String email) {
		return studentRepository.existsByEmail(email);
	}
	
	@Override
	public boolean isEmailExistForOther(Long studentId, String email) {
	    Student existing = studentRepository.findByEmail(email).get();
	    return existing != null && !existing.getStudentId().equals(studentId);
	}
	
	@Override
	public boolean isPhoneExist(String phoneNumber) {
	    return studentRepository.existsByPhoneNumber(phoneNumber);
	}

	@Override
	public boolean isPhoneExistForOther(Long studentId, String phoneNumber) {
	    return studentRepository.findByPhoneNumber(phoneNumber)
	            .map(existing -> !existing.getStudentId().equals(studentId))
	            .orElse(false);
	}

	
}
