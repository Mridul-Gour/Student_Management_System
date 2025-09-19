package com.cts.sms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"department", "students", "subjects"})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String courseName;
    private String courseCode;

    
    // Students enrolled in this course
    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<Student> students;

    // Subjects under this course
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Subject> subjects;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
