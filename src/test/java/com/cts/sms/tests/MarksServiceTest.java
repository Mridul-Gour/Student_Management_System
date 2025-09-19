//package com.cts.sms.modules.academicperformance;
//
//import com.cts.sms.modules.academicperformance.dto.MarksDTO;
//import com.cts.sms.modules.academicperformance.model.Marks;
//import com.cts.sms.modules.academicperformance.repository.MarksRepository;
//import com.cts.sms.modules.academicperformance.service.MarksServiceImpl;
//import com.cts.sms.modules.studentmanagement.model.Student;
//import com.cts.sms.modules.studentmanagement.repository.StudentRepository;
//import com.cts.sms.modules.course.model.Subject;
//import com.cts.sms.modules.course.repository.SubjectRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class MarksServiceTest {
//
//    @Mock
//    private MarksRepository marksRepository;
//
//    @Mock
//    private StudentRepository studentRepository;
//
//    @Mock
//    private SubjectRepository subjectRepository;
//
//    @InjectMocks
//    private MarksServiceImpl marksServiceImpl;
//
//    private Marks mockMarks;
//    private MarksDTO mockMarksDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Initialize mock Marks entity
//        mockMarks = new Marks(1L, 101L, 4L, 301L, 90, "Final", "Sem1");
//
//
//        // Initialize mock MarksDTO consistent with entity
//        mockMarksDTO = new MarksDTO();
//        mockMarksDTO.setMarksId(1L);
//        mockMarksDTO.setStudentId(101L);
////        mockMarksDTO.setCourseId(4); // if applicable in DTO
//        mockMarksDTO.setSubjectId(301L);
//        mockMarksDTO.setMarksObtained(90);
//        mockMarksDTO.setExamType("Final");
//        mockMarksDTO.setSemester("Sem1");
//        mockMarksDTO.setStudentName("John Doe");  // if DTO contains this field
//        mockMarksDTO.setSubjectName("Physics");   // if DTO contains this field
//
//        // Mock student entity
//        Student mockStudent = new Student();
//        mockStudent.setFirstName("John");
//        mockStudent.setLastName("Doe");
//
//        // Mock subject entity
//        Subject mockSubject = new Subject();
//        mockSubject.setSubjectName("Physics");
//
//        when(studentRepository.findById(101L)).thenReturn(Optional.of(mockStudent));
//        when(subjectRepository.findById(301L)).thenReturn(Optional.of(mockSubject));
//    }
//
//    @Test
//    void testAddMarks() {
//        when(marksRepository.save(any(Marks.class))).thenReturn(mockMarks);
//
//        MarksDTO saved = marksServiceImpl.addMarks(mockMarksDTO);
//
//        assertNotNull(saved);
//        assertEquals(90, saved.getMarksObtained());
//        assertEquals("John Doe", saved.getStudentName());
//        assertEquals("Physics", saved.getSubjectName());
//        verify(marksRepository, times(1)).save(any(Marks.class));
//    }
//
//    @Test
//    void testGetAllMarks() {
//        when(marksRepository.findAll()).thenReturn(List.of(mockMarks));
//
//        List<MarksDTO> marksList = marksServiceImpl.getAllMarks();
//
//        assertEquals(1, marksList.size());
//        assertEquals(101L, marksList.get(0).getStudentId());
//        assertEquals("Physics", marksList.get(0).getSubjectName());
//    }
//
//    @Test
//    void testGetAllMarks_EmptyList() {
//        when(marksRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<MarksDTO> marksList = marksServiceImpl.getAllMarks();
//
//        assertTrue(marksList.isEmpty());
//    }
//
//    @Test
//    void testGetMarksById_Found() {
//        when(marksRepository.findById(1L)).thenReturn(Optional.of(mockMarks));
//
//        MarksDTO found = marksServiceImpl.getMarksById(1L);
//
//        assertNotNull(found);
//        assertEquals("Final", found.getExamType());
//        assertEquals("Physics", found.getSubjectName());
//    }
//
//    @Test
//    void testGetMarksById_NotFound() {
//        when(marksRepository.findById(2L)).thenReturn(Optional.empty());
//
//        Exception ex = assertThrows(RuntimeException.class, () -> {
//            marksServiceImpl.getMarksById(2L);
//        });
//
//        assertEquals("Marks not found with id: 2", ex.getMessage());
//    }
//
//    @Test
//    void testCalculateAverageMarks() {
//        when(marksRepository.findByStudentId(101L)).thenReturn(List.of(mockMarks));
//
//        double avg = marksServiceImpl.calculateAverageMarks(101L);
//
//        assertEquals(90.0, avg);
//    }
//
//    @Test
//    void testCalculateGrade() {
//        when(marksRepository.findByStudentId(101L)).thenReturn(List.of(mockMarks));
//
//        String grade = marksServiceImpl.calculateGrade(101L);
//
//        assertEquals("A+", grade);
//    }
//
//    @Test
//    void testCalculateGPA() {
//        when(marksRepository.findByStudentIdAndSemester(101L, "Sem1")).thenReturn(List.of(mockMarks));
//
//        double gpa = marksServiceImpl.calculateGPA(101L, "Sem1");
//
//        // Service uses scale of 10, so 90% = 9.0
//        assertEquals(9.0, gpa, 0.01);
//    }




