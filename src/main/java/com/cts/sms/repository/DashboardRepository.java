package com.cts.sms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository {
    // Future: custom queries to aggregate data across tables
	@Query("SELECT AVG(m.gpa) FROM Marks m WHERE m.studentId = :studentId")
	double findAverageGpaByStudentId(Long studentId);
}
