package com.cts.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private String subjectCode;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    @ManyToMany(mappedBy = "subjects")
    @JsonIgnore
    private List<Student> students;

    // 🔑 Remove associations before deleting subject
    @PreRemove
    private void removeStudentAssociations() {
        for (Student s : students) {
            s.getSubjects().remove(this);
        }
        students.clear();
    }
}
