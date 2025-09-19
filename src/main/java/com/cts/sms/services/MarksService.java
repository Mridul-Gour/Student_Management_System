package com.cts.sms.services;

import com.cts.sms.dto.MarksDTO;
import com.cts.sms.model.Marks;

import java.util.List;

public interface MarksService {
    MarksDTO addMarks(MarksDTO marksDTO);
    List<MarksDTO> getAllMarks();
    MarksDTO getMarksById(Long id);
    List<MarksDTO> getMarksByStudent(Long studentId);
    void deleteMarks(Long id);
    MarksDTO updateMarks(Long id, MarksDTO updatedMarks);

    // Updated signatures with semester parameter
    double calculateAverageMarks(Long studentId, String semester);
    String calculateGrade(Long studentId, String semester);
    double calculateGPA(Long studentId, String semester);

    // Average & GPA methods
    double calculateAverageMarks(Long studentId); // all semesters
	double getStudentGPA(Long studentId);
	
	double calculateOverallGPA(Long studentId);
	
	//student dashboard	
	List<Marks> getMarksByStudentId(Long studentId);
	Marks getMarksEntityById(Long marksId);
	Marks saveMarks(Marks marks);

}
