package com.cts.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.sms.model.Marks;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    // Get all marks for a specific student
    List<Marks> findByStudentId(Long studentId);

    // Get all marks for a specific student and semester
    List<Marks> findByStudentIdAndSemester(Long studentId, String semester);

    // Optional: get all marks for a specific subject
    List<Marks> findBySubjectId(Long subjectId);

    // Optional: get all marks for a specific course
    List<Marks> findByCourseId(Long courseId);
    
    
    
    
    


}
