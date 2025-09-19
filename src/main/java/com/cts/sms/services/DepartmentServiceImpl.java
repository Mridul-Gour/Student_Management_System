package com.cts.sms.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.sms.model.Department;
import com.cts.sms.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepo;
	
	@Override
	public List<Department> getAllDepartments() {
		// TODO Auto-generated method stub
		return departmentRepo.findAll();
	}

	@Override
	public Department getDepartmentById(Long id) {
		// TODO Auto-generated method stub
		return departmentRepo.findById(id).orElseThrow(() -> new RuntimeException("Department Not found"));
	}

	@Override
	public Department saveDepartment(Department department) {
		// TODO Auto-generated method stub
		return departmentRepo.save(department);
	}

	@Override
	public void deleteDepartment(Long id) {
		// TODO Auto-generated method stub
		departmentRepo.deleteById(id);
		
	}

	@Override
	public boolean isDepartmentNameExist(String name) {
		// TODO Auto-generated method stub
		return departmentRepo.findByName(name).isPresent();
	}

	@Override
	public boolean isDepartmentNameExistForOther(Long id, String name) {
		// TODO Auto-generated method stub
		 Optional<Department> existing = departmentRepo.findByName(name);
	     return existing.isPresent() && !existing.get().getDepartmentId().equals(id);
	}

}
