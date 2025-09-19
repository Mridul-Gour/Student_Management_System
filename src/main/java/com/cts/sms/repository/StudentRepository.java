package com.cts.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.sms.model.Student;

import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	@Modifying
	@Transactional
	@Query( 
			value = "UPDATE student SET email = ?1 WHERE student_id = ?2",
			nativeQuery = true
	)
	public int updateStudentEmailByStudentId(String email, Long id);
	
	@Query("SELECT s FROM Student s WHERE s.firstName = :name OR s.lastName = :name ")
	public List<Student> findByFirstNameOrLastName(String name);
	
	public List<Student> findByDepartment_NameContainingIgnoreCase(String departmentName);
	
	public List<Student> findByYear(Integer year);
		
	@Query("SELECT s FROM Student s " +
	           "WHERE (:firstName IS NULL OR s.firstName = :firstName) " +
	           "OR (:lastName IS NULL OR s.lastName = :lastName) " +
	           "OR (:name IS NULL OR s.department.name = :name) " +
	           "OR (:year IS NULL OR s.year = :year)")
	public List<Student> findByNameOrDepartmentOrYear(
	            @Param("firstName") String firstName,
	            @Param("lastName") String lastName,
	            @Param("name") String name,
	            @Param("year") Integer year
	    );	
	
	public List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrYear(
		    String firstName,
		    String lastName,
		    String email,
		    String departmentName,
		    Integer year 
		);
	
	public List<Student> findTop5ByOrderByStudentIdDesc();
	
	public Optional<Student> findByEmail(String email);
	public List<Student> findByCourses_Id(Long courseId);
	
	@Query("SELECT s FROM Student s JOIN s.subjects subj WHERE subj.id = :subjectId")
	public List<Student> findStudentsBySubject(@Param("subjectId") Long subjectId);
	
	public boolean existsByEmail(String email);
	
	boolean existsByPhoneNumber(String phoneNumber);
	
    Optional<Student> findByPhoneNumber(String phoneNumber);

}

	

