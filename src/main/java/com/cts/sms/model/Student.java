package com.cts.sms.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "student")
@ToString(exclude = {"marks", "attendance", "user"})
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentId;
	
	@NotBlank(message = "First name is required")
	@Size(min = 2, max = 50, message = "First name must be 2-50 characters")
	@Column(nullable = false)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
	@Column(nullable = false)
	private String lastName;
	
	@NotBlank(message = "Gender is required")
	@Column(nullable = false)
	private String gender;
	
	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	@Column(nullable = false, unique = true)
	private String email;
	
	@Size(min = 10, max = 100, message = "Address must be 10-50 characters")
	@NotBlank(message = "Address is required")
	@Column(nullable = false)
	private String address;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "\\d{10}", message = "Enter a valid phone number")
	@Size(min = 10, max = 10,  message = "Phone number must be 10 digits")
	@Column(nullable = false)
	private String phoneNumber;
	
	@Positive(message = "Year must be greater than 0")
	@NotNull(message = "Year is required")
	@Column(nullable = false)
	private Integer year;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id", referencedColumnName = "departmentId")
	private Department department;
	
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	
	@ManyToMany
	@JoinTable(
	    name = "student_courses",
	    joinColumns = @JoinColumn(name = "student_id"),
	    inverseJoinColumns = @JoinColumn(name = "course_id")
	)
	private List<Course> courses = new ArrayList<>();
	
	
	@ManyToMany
	@JoinTable(
	    name = "student_subjects",
	    joinColumns = @JoinColumn(name = "student_id"),
	    inverseJoinColumns = @JoinColumn(name = "subject_id")
	)
	private List<Subject> subjects = new ArrayList<>();
	
	@OneToMany(mappedBy = "studentId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Marks> marks;

	
	@Transient
	private List<Long> courseIds;

	@Transient
	private List<Long> subjectIds;
	
	@Transient
	private Long departmentId;
	// + getters and setters


	

}
