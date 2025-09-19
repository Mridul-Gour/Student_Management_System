package com.cts.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.sms.model.Department;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
	
	Optional<Department> findByCode(String code);
	
	Optional<Department> findByName(String departmentName);
	
}
