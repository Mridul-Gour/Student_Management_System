package com.cts.sms.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.sms.exception.StudentNotFoundException;
import com.cts.sms.model.Department;
import com.cts.sms.model.Student;
import com.cts.sms.repository.DepartmentRepository;
import com.cts.sms.repository.StudentRepository;
import com.cts.sms.services.StudentServiceImpl;

public class StudentServiceTest {
	
	@Mock
	private StudentRepository studentRepository;
	
	@Mock
	private DepartmentRepository departmentRepository;
	
	@InjectMocks
	private StudentServiceImpl studentService;
	
	private Student student;
	
	private Department department;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		department = new Department();
		
		department.setDepartmentId(1L);
		department.setName("Computer Science");
		
		student = new Student();
		student.setStudentId(1L);
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("john@gmail.com");
		student.setDepartment(department);
		student.setYear(2);
	}
	
	@Test
	void testSaveStudent()
	{
		when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
	    when(studentRepository.save(any(Student.class))).thenReturn(student);

	    Student saved = studentService.saveStudent(student);

	    assertNotNull(saved);
	    assertEquals("John", saved.getFirstName());
	    verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
    void testGetStudentById_Found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }
	
	@Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(2L));
    }
	
	@Test
    void testUpdateStudent_WithDepartment() {
        Student updated = new Student();
        updated.setFirstName("Jane");
        updated.setDepartment(department);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateStudent(1L, updated);

        assertEquals("Jane", result.getFirstName());
        verify(studentRepository).save(student);
    }
	
	@Test
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Student> students = studentService.getAllStudents();

        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());
    }

    @Test
    void testSearchStudentByName() {
        when(studentRepository.findByFirstNameOrLastName("John"))
                .thenReturn(Arrays.asList(student));

        List<Student> result = studentService.searchStudentByName("John");

        assertEquals(1, result.size());
        verify(studentRepository).findByFirstNameOrLastName("John");
    }

    @Test
    void testSearchStudentByDepartment() {
        when(studentRepository.findByDepartment_NameContainingIgnoreCase("Computer"))
                .thenReturn(Arrays.asList(student));

        List<Student> result = studentService.searchStudentByDepartment("Computer");

        assertEquals(1, result.size());
        verify(studentRepository).findByDepartment_NameContainingIgnoreCase("Computer");
    }

    @Test
    void testSearchStudentByYear() {
        when(studentRepository.findByYear(2023))
                .thenReturn(Arrays.asList(student));

        List<Student> result = studentService.searchStudentByYear(2023);

        assertEquals(1, result.size());
        verify(studentRepository).findByYear(2023);
    }

    @Test
    void testSearchStudents_WithKeywordAsName() {
        when(studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrYear(
                "John", "John", "John", "John", null))
                .thenReturn(Arrays.asList(student));

        List<Student> result = studentService.searchStudents("John");

        assertEquals(1, result.size());
    }

    @Test
    void testSearchStudents_WithKeywordAsYear() {
        when(studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrYear(
                "2023", "2023", "2023", "2023", 2023))
                .thenReturn(Arrays.asList(student));

        List<Student> result = studentService.searchStudents("2023");

        assertEquals(1, result.size());
    }
}
	

