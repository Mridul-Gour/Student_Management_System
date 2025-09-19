package com.cts.sms.controller;

import com.cts.sms.model.Department;
import com.cts.sms.services.DepartmentService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService; 

    @GetMapping
    public String listDepartments(Model model) {
    	 List<Department> depts = departmentService.getAllDepartments();
    	    System.out.println(depts); // Debug: check console
    	    model.addAttribute("departments", depts);
    	    return"department";

    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "department-form";
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "department-form";
        }

        if (department.getDepartmentId() == null && departmentService.isDepartmentNameExist(department.getName())) {
            model.addAttribute("errorMessage", "Department already exists!");
            return "department-form";
        } else if (department.getDepartmentId() != null &&
                   departmentService.isDepartmentNameExistForOther(department.getDepartmentId(), department.getName())) {
            model.addAttribute("errorMessage", "Department already exists!");
            return "department-form";
        }

        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentById(id));
        return "department-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/departments";
    }
}

