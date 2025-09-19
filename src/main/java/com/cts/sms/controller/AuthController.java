package com.cts.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.cts.sms.dto.UserRequestDTO;
import com.cts.sms.model.Role;
import com.cts.sms.model.User;
import com.cts.sms.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------- LOGIN ----------------
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login"; // maps to login.html
    }

    // ---------------- REGISTER ----------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRequestDTO()); // Use DTO here instead of entity
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRequestDTO userDTO,
                               BindingResult result,
                               Model model) {

        // Field validation errors (password, etc.)
        if (result.hasErrors()) {
            return "register";
        }

        // Duplicate email check
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists!");
            return "register";
        }

        // Create new user (Force STUDENT role)
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.STUDENT); // ✅ Always assign STUDENT role
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        model.addAttribute("successMessage", "Registration successful! Please log in.");
        return "login";
    }
}
// //        assertEquals("Physics", saved.getSubjectName());