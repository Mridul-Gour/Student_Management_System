package com.cts.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.sms.model.Subject;

import jakarta.transaction.Transactional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByCourse_Id(Long courseId);

    // 🔑 Remove ManyToMany links from student_subjects table
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_subjects WHERE subject_id = :subjectId", nativeQuery = true)
    void removeStudentLinks(@Param("subjectId") Long subjectId);
    
 // 🔑 Remove all student-subject links for subjects under a specific course
    @Modifying
    @Transactional
    @Query(value = "DELETE ss FROM student_subjects ss " +
                   "JOIN subject s ON ss.subject_id = s.id " +
                   "WHERE s.course_id = :courseId", nativeQuery = true)
    void removeAllStudentLinksByCourse(@Param("courseId") Long courseId);

    
    
  
}
