package com.cts.sms.services;

import com.cts.sms.model.Subject;
import com.cts.sms.repository.SubjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepo;

    public Subject saveSubject(Subject subject) {
        return subjectRepo.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepo.findAll();
    }

    public Subject findById(Long id) {
        return subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        // 1️⃣ Remove links from student_subjects table
        subjectRepo.removeStudentLinks(id);

        // 2️⃣ Now delete the Subject safely
        subjectRepo.delete(subject);
    }

    public List<Subject> findAllById(List<Long> ids) {
        return subjectRepo.findAllById(ids);
    }

    public List<Subject> getSubjectsByCourse(Long courseId) {
        return subjectRepo.findByCourse_Id(courseId);
    }
    public Subject getSubjectById(Long id) {
        return subjectRepo.findById(id).orElse(null);
    }
}
