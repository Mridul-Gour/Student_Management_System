package com.cts.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "marks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marks_id")
    private Long marksId;

    @NotNull(message = "Student ID cannot be null")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @NotNull(message = "Course ID cannot be null")
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @NotNull(message = "Subject ID cannot be null")
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Min(value = 0, message = "Marks cannot be less than 0")
    @Max(value = 100, message = "Marks cannot be more than 100")
    @Column(nullable = false, name = "marks_obtained")
    private Integer marksObtained;

    @NotBlank(message = "Exam type is required")
    @Column(nullable = false, length = 50, name = "exam_type")
    private String examType;

    @NotBlank(message = "Semester is required")
    @Column(nullable = false, length = 10, name = "semester")
    private String semester;

    // Relationship to Subject for Thymeleaf display
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;
    
    @Transient
    private String subjectName; // used only for displaying in Thymeleaf

}
