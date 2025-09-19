package com.cts.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.sms.model.Course;

import jakarta.transaction.Transactional;

public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
	List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);
	
	 Optional<Course> findByCourseCode(String courseCode);
	 
	 List<Course> findByDepartment_DepartmentId(Long departmentId);
	  @Modifying
	    @Transactional
	    @Query(value = "DELETE FROM student_courses WHERE course_id = :courseId", nativeQuery = true)
	    void removeAllStudentLinks(@Param("courseId") Long courseId);

	 
	 
}
