# 🎓 Student Management System

## 📌 Overview
The **Student Management System (SMS)** is a full-stack web application designed to manage student records, courses, attendance, and academic performance.  
It supports two roles: **Admin** and **Student**, each with role-based access and dashboards.

---

## 🎯 Objective
- Admin, Faculty, Student users — different roles.
- Store/manage student data, attendance, grades, courses.
- Authentication & authorization with JWT.
- Centralized dashboard for each role.

---

## 🏗️ Project Modules (7 total)
### User Management Module
- Register/Login users (**Admin, Faculty, Student**)
- JWT-based authentication
- Update profile/password

### Student Information Module
- Admin adds/edits student records
- View/search student profiles

### Course & Subject Module
- Admin creates courses & subjects
- Assign subjects to students/faculty
- View course catalog

### Academic Performance Module
- Faculty adds marks/grades
- GPA calculation
- Student views report card

### Attendance Module
- Faculty marks attendance
- Students see attendance summary
- Reports for Admin

### Security Module (JWT)
- Handles login, token generation
- Role-based access control

### Dashboard Module
Role-based dashboards:
- **Admin Dashboard** → Statistics of system (counts, recent data)
- **Student Dashboard** → Personal info, attendance %, GPA, enrolled courses  
- Acts as the *home page after login*

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot (Java 17, Spring MVC, Spring Data JPA, Security/JWT) 
- **Frontend:** Thymeleaf, HTML, CSS, Bootstrap  
- **Database:** MySQL (via Spring Data JPA)  
- **Security:** Spring Security with JWT  
- **Build Tool:** Maven   

---

## 👥 Contributors
**Team 6 – Student Management System Project**  
- Peeyush Pandey(Team Leader)
- Niharika Sahu
- Mridul Gour
- Latha Vangimalli
- Lohith Gantla
