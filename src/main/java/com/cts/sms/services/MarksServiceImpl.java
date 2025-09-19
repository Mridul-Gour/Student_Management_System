package com.cts.sms.services;

import com.cts.sms.dto.MarksDTO;
import com.cts.sms.model.Marks;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.MarksRepository;
import com.cts.sms.repository.StudentRepository;
import com.cts.sms.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksServiceImpl implements MarksService {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public MarksServiceImpl(MarksRepository marksRepository,
                            StudentRepository studentRepository,
                            CourseRepository courseRepository,
                            SubjectRepository subjectRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
    }

    // ========================= DTO Conversion =========================
    private MarksDTO convertToDTO(Marks marks) {
        MarksDTO dto = new MarksDTO();
        dto.setMarksId(marks.getMarksId());
        dto.setStudentId(marks.getStudentId());
        dto.setCourseId(marks.getCourseId());
        dto.setSubjectId(marks.getSubjectId());
        dto.setMarksObtained(marks.getMarksObtained());
        dto.setExamType(marks.getExamType());
        dto.setSemester(marks.getSemester());

        studentRepository.findById(marks.getStudentId())
                .ifPresent(s -> dto.setStudentName(s.getFirstName() + " " + s.getLastName()));

        courseRepository.findById(marks.getCourseId())
                .ifPresent(c -> dto.setCourseName(c.getCourseName()));

        subjectRepository.findById(marks.getSubjectId())
                .ifPresent(s -> dto.setSubjectName(s.getSubjectName()));

        return dto;
    }

    private Marks convertToEntity(MarksDTO dto) {
        Marks marks = new Marks();
        marks.setMarksId(dto.getMarksId());
        marks.setStudentId(dto.getStudentId());
        marks.setCourseId(dto.getCourseId());
        marks.setSubjectId(dto.getSubjectId());
        marks.setMarksObtained(dto.getMarksObtained());
        marks.setExamType(dto.getExamType());
        marks.setSemester(dto.getSemester());
        return marks;
    }

    // ========================= Validation =========================
    private void validateStudentCourseSubject(Long studentId, Long courseId, Long subjectId) {
        boolean courseAssigned = courseRepository.findCoursesByStudentId(studentId)
                .stream().anyMatch(c -> c.getId().equals(courseId));
        if (!courseAssigned) {
            throw new IllegalArgumentException("Course is not assigned to this student.");
        }

        boolean subjectAssigned = subjectRepository.findById(subjectId)
                .filter(s -> s.getStudents().stream().anyMatch(st -> st.getStudentId().equals(studentId)))
                .isPresent();
        if (!subjectAssigned) {
            throw new IllegalArgumentException("Subject is not assigned to this student.");
        }
    }

    // ========================= ADD =========================
    @Override
    public MarksDTO addMarks(MarksDTO marksDTO) {
        validateStudentCourseSubject(marksDTO.getStudentId(), marksDTO.getCourseId(), marksDTO.getSubjectId());

        // Check duplicate marks for same student-course-subject-semester-exam
        boolean duplicateExists = marksRepository.findByStudentId(marksDTO.getStudentId()).stream()
                .anyMatch(m ->
                        m.getCourseId().equals(marksDTO.getCourseId()) &&
                        m.getSubjectId().equals(marksDTO.getSubjectId()) &&
                        m.getSemester().equals(marksDTO.getSemester()) &&
                        m.getExamType().equals(marksDTO.getExamType())
                );
        if (duplicateExists) {
            throw new IllegalArgumentException("This student already has marks for this course, subject, semester, and exam type.");
        }

        Marks marks = convertToEntity(marksDTO);
        return convertToDTO(marksRepository.save(marks));
    }

    // ========================= UPDATE =========================
    @Override
    public MarksDTO updateMarks(Long id, MarksDTO updatedMarks) {
        return marksRepository.findById(id)
                .map(existing -> {

                    validateStudentCourseSubject(updatedMarks.getStudentId(),
                            updatedMarks.getCourseId(),
                            updatedMarks.getSubjectId());

                    // Duplicate check excluding current record
                    boolean duplicateExists = marksRepository.findByStudentId(updatedMarks.getStudentId()).stream()
                            .anyMatch(m ->
                                    !m.getMarksId().equals(id) &&
                                    m.getCourseId().equals(updatedMarks.getCourseId()) &&
                                    m.getSubjectId().equals(updatedMarks.getSubjectId()) &&
                                    m.getSemester().equals(updatedMarks.getSemester()) &&
                                    m.getExamType().equals(updatedMarks.getExamType())
                            );
                    if (duplicateExists) {
                        throw new IllegalArgumentException("This student already has marks for this course, subject, semester, and exam type.");
                    }

                    // Update
                    existing.setStudentId(updatedMarks.getStudentId());
                    existing.setCourseId(updatedMarks.getCourseId());
                    existing.setSubjectId(updatedMarks.getSubjectId());
                    existing.setMarksObtained(updatedMarks.getMarksObtained());
                    existing.setExamType(updatedMarks.getExamType());
                    existing.setSemester(updatedMarks.getSemester());

                    return convertToDTO(marksRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Marks not found with id: " + id));
    }

    // ========================= GET =========================
    @Override
    public List<MarksDTO> getAllMarks() {
        return marksRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MarksDTO getMarksById(Long id) {
        return marksRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Marks not found with id: " + id));
    }

    @Override
    public List<MarksDTO> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ========================= DELETE =========================
    @Override
    public void deleteMarks(Long id) {
        marksRepository.deleteById(id);
    }

    // ========================= CALCULATIONS =========================
    @Override
    public double calculateAverageMarks(Long studentId, String semester) {
        return marksRepository.findByStudentIdAndSemester(studentId, semester).stream()
                .mapToDouble(Marks::getMarksObtained)
                .average().orElse(0.0);
    }

    @Override
    public String calculateGrade(Long studentId, String semester) {
        double avg = calculateAverageMarks(studentId, semester);
        if (avg >= 90) return "A+";
        if (avg >= 75) return "A";
        if (avg >= 60) return "B";
        if (avg >= 40) return "C";
        return "F";
    }

    @Override
    public double calculateGPA(Long studentId, String semester) {
        List<Marks> marksList = marksRepository.findByStudentIdAndSemester(studentId, semester);
        if (marksList.isEmpty()) return 0.0;

        double totalPoints = marksList.stream()
                .mapToDouble(m -> (m.getMarksObtained() / 100.0) * 10.0)
                .sum();

        
        return totalPoints / marksList.size();
    }

    @Override
    public double calculateAverageMarks(Long studentId) {
        return marksRepository.findByStudentId(studentId).stream()
                .mapToDouble(Marks::getMarksObtained)
                .average().orElse(0.0);
    }

    @Override
    public double getStudentGPA(Long studentId) {
        List<Marks> marksList = marksRepository.findByStudentId(studentId);
        if (marksList.isEmpty()) return 0.0;

        double totalPoints = marksList.stream()
                .mapToDouble(m -> (m.getMarksObtained() / 100.0) * 10.0)
                .sum();

        return totalPoints / marksList.size();
    }

    @Override
    public List<Marks> getMarksByStudentId(Long studentId) {
        // Fetch all marks for the given student
        return marksRepository.findByStudentId(studentId);
    }

    @Override
    public Marks getMarksEntityById(Long marksId) {
        // Fetch a specific marks entity by its ID, return null if not found
        return marksRepository.findById(marksId).orElse(null);
    }

    @Override
    public Marks saveMarks(Marks marks) {
        // Save or update a marks entity
        return marksRepository.save(marks);
    }

    @Override
    public double calculateOverallGPA(Long studentId) {
        List<Marks> marksList = marksRepository.findByStudentId(studentId);
        if (marksList.isEmpty()) return 0.0;

        double totalPoints = 0.0;
        for (Marks m : marksList) {
            double gpaPoints = (m.getMarksObtained() / 100.0) * 10.0; // scale 0–10
            totalPoints += gpaPoints;
        }
        return totalPoints / marksList.size();
    }
    
}
