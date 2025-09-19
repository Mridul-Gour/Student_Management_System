package com.cts.sms.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMarksForm {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Marks cannot be null")
    @Min(value = 0, message = "Marks cannot be less than 0")
    @Max(value = 100, message = "Marks cannot be more than 100")
    private Integer marksObtained;

    @NotBlank(message = "Exam type is required")
    private String examType;

    @NotBlank(message = "Semester is required")
    private String semester;
    
    @NotNull
    private Long marksId;
}
