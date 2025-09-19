package com.cts.sms.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cts.sms.repository.StudentRepository;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.SubjectRepository;
import com.cts.sms.services.MarksService;
import com.cts.sms.services.DashboardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DashboardServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private MarksService marksService;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test 1: Student count should match repository
    @Test
    void testGetStudentCount() {
        when(studentRepository.count()).thenReturn(5L);
        assertEquals(5, dashboardService.getStudentCount());
    }

    // Test 2: Course count should match repository
    @Test
    void testGetCourseCount() {
        when(courseRepository.count()).thenReturn(3L);
        assertEquals(3, dashboardService.getCourseCount());
    }

     //Test 3: GPA calculation for a student
    @Test
    void testGetStudentGPA() {
        when(marksService.calculateGPA(1L, "2025")).thenReturn(8.5);
        assertEquals(8.5, dashboardService.getStudentGPA(1L));
    }

}
