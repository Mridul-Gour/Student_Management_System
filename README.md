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
- Acts as the home page after login

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot (Java 17, Spring MVC, Spring Data JPA, Security/JWT) 
- **Frontend:** Thymeleaf, HTML, CSS, Bootstrap  
- **Database:** MySQL (via Spring Data JPA)  
- **Security:** Spring Security with JWT  
- **Build Tool:** Maven   

---

## ⚡ Getting Started

### Prerequisites
- JDK 17+
- Maven
- MySQL

---

## Screenshots

### Admin Dashboard
<img width="1920" height="976" alt="Screenshot (1638)" src="https://github.com/user-attachments/assets/d863c269-59cf-43b3-b17a-4c63b1d4b3dd" />
<img width="1920" height="990" alt="Screenshot (1639)" src="https://github.com/user-attachments/assets/7adc9aaf-8686-4765-a09a-04f7bfedf8eb" />

### Student Dashboard
<img width="1881" height="913" alt="Screenshot 2025-09-09 195008" src="https://github.com/user-attachments/assets/974a1861-8e35-4adf-b69f-05058ee18c94" />
<img width="1876" height="914" alt="Screenshot 2025-09-09 195021" src="https://github.com/user-attachments/assets/988078f4-4519-4cb4-9b39-4e2b97008173" />

### Academic Performance
<img width="1736" height="609" alt="Screenshot 2025-09-13 214140" src="https://github.com/user-attachments/assets/952228f9-9954-435b-bde9-f95c983af5f0" />
<img width="1744" height="937" alt="Screenshot 2025-09-09 161227" src="https://github.com/user-attachments/assets/c6cfffef-c20e-4685-b56c-67fb0a851820" />

### Attendance
<img width="1337" height="659" alt="Screenshot 2025-09-16 003209" src="https://github.com/user-attachments/assets/7c6e8066-e7a8-4308-8f0c-ae444860c783" />
<img width="1354" height="407" alt="Screenshot 2025-09-16 002853" src="https://github.com/user-attachments/assets/378d10a7-8a6c-4000-b53d-5b613c456ff1" />

### User
<img width="599" height="872" alt="Screenshot 2025-09-13 212242" src="https://github.com/user-attachments/assets/fcb0d291-7db5-44ab-b4a4-dbf4819a7aed" />

### Student
<img width="498" height="672" alt="Screenshot 2025-09-10 020844" src="https://github.com/user-attachments/assets/16fdbdfb-dad6-4d7a-9980-b544ea44e5bc" />


## Run the project
```bash
mvn spring-boot:run
