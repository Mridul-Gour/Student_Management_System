package com.cts.sms.services;

import java.util.List;

import com.cts.sms.model.Department;

public interface DepartmentService {
	
	public List<Department> getAllDepartments();
	
	public Department getDepartmentById(Long id);
	
    Department saveDepartment(Department department);

    void deleteDepartment(Long id);

    boolean isDepartmentNameExist(String name);

    boolean isDepartmentNameExistForOther(Long id, String name);
	
}
