package com.cts.sms.services;

import com.cts.sms.exception.CourseActionException;
import com.cts.sms.model.Course;
import com.cts.sms.model.Student;
import com.cts.sms.model.Subject;
import com.cts.sms.repository.CourseRepository;
import com.cts.sms.repository.SubjectRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    public Course saveCourse(Course course) {
        return courseRepo.save(course);
    }

    public Course findById(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new CourseActionException("Course not found with id: " + id));
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public List<Course> findAllById(List<Long> ids) {
        return courseRepo.findAllById(ids);
    }

    // ✅ Delete course safely
    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = findById(courseId);

        // Block deletion if students are enrolled
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            throw new CourseActionException(
                    "Cannot delete course '" + course.getCourseName() +
                            "' because " + course.getStudents().size() + " student(s) are enrolled."
            );
        }

        // Remove subject-student links
        if (course.getSubjects() != null) {
            for (Subject subject : course.getSubjects()) {
                if (subject.getStudents() != null) {
                    for (Student student : subject.getStudents()) {
                        student.getSubjects().remove(subject);
                    }
                }
            }
        }

        // Delete the course (subjects deleted via cascade + orphanRemoval)
        courseRepo.delete(course);
    }

    @Transactional
    public Course updateCourse(Long courseId, Course updatedCourse) {
        // 1️⃣ Fetch existing course
        Course existingCourse = findById(courseId);

        // 2️⃣ Block update if students are enrolled
        if (existingCourse.getStudents() != null && !existingCourse.getStudents().isEmpty()) {
            throw new CourseActionException(
                "Cannot update course '" + existingCourse.getCourseName() +
                "' because " + existingCourse.getStudents().size() + " student(s) are enrolled."
            );
        }

        // 3️⃣ Update basic fields
        existingCourse.setCourseName(updatedCourse.getCourseName());
        existingCourse.setCourseCode(updatedCourse.getCourseCode());
        existingCourse.setDepartment(updatedCourse.getDepartment());

        // 4️⃣ Update subjects safely
        List<Subject> updatedSubjects = updatedCourse.getSubjects();
        if (updatedSubjects == null) updatedSubjects = List.of(); // empty list

        // Make a copy of current subjects to avoid concurrent modification
        List<Subject> currentSubjects = List.copyOf(existingCourse.getSubjects());

        // Remove subjects not in updatedSubjects
        for (Subject subject : currentSubjects) {
            if (!updatedSubjects.contains(subject)) {
                existingCourse.getSubjects().remove(subject);
                subject.setCourse(null); // disconnect orphan properly
            }
        }

        // Add new subjects
        for (Subject subject : updatedSubjects) {
            if (!existingCourse.getSubjects().contains(subject)) {
                existingCourse.getSubjects().add(subject);
                subject.setCourse(existingCourse);
            }
        }

        return courseRepo.save(existingCourse);
    }

    public boolean isCourseCodeExist(String courseCode) {
        return courseRepo.findByCourseCode(courseCode).isPresent();
    }

    public List<Course> getByDepartmentId(Long departmentId) {
        return courseRepo.findByDepartment_DepartmentId(departmentId);
    }
}
