package com.cts.sms.services;

import java.util.List;
import java.util.Optional;

import com.cts.sms.model.Student;
import com.cts.sms.repository.StudentRepository;


public interface StudentService {
	
	public Student saveStudent(Student student);
		
	public Student updateStudent(Long id, Student student);
	
	public void deleteStudent(Long id);
	
	public Student getStudentById(Long id);
	
	public List<Student> getAllStudents();
		
	public List<Student> searchStudentByName(String name);
		
	public List<Student> searchStudentByYear(Integer year);

	public List<Student> searchStudentByDepartment(String department);

	public List<Student> searchStudents(String keyword);
	
    Student getStudentByEmail(String email);

    Long getStudentIdByEmail(String email);
    
    List<Student> getStudentsByCourse(Long courseId); 
    List<Student> getStudentsBySubject(Long subjectId);
    
    public boolean isEmailExist(String email);
    
    public boolean isEmailExistForOther(Long studentId, String email);
    
    boolean isPhoneExist(String phoneNumber);
	
    boolean isPhoneExistForOther(Long studentId, String phoneNumber);
    
    
    

    


	
}
