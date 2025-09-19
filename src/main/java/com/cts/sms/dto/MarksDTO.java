package com.cts.sms.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksDTO {
    private Long marksId;
    private Long studentId;
    private Long courseId;
    private Long subjectId;
    private int marksObtained;
    private String examType;
    private String semester;

    // Display fields
    private String studentName;
    private String courseName;
    private String subjectName;
}
