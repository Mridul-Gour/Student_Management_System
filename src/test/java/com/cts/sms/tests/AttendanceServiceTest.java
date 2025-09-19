package com.cts.sms.tests;

import com.cts.sms.model.Attendance;
import com.cts.sms.repository.AttendanceRepository;
import com.cts.sms.services.AttendanceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Attendance attendance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        attendance = new Attendance();
        attendance.setId(1L);
        attendance.setStudentId(101L);
        attendance.setSubjectId(501L);
        attendance.setDate(LocalDate.now());
        attendance.setPresent(true);
    }

    @Test
    void testMarkAttendance() {
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance saved = attendanceService.markAttendance(101L, 501L, true);

        assertEquals(101L, saved.getStudentId());
        assertEquals(true, saved.getPresent());
    }

    @Test
    void testGetAttendanceByStudent() {
        when(attendanceRepository.findByStudentId(101L)).thenReturn(Arrays.asList(attendance));

        List<Attendance> list = attendanceService.getAttendanceByStudent(101L);

        assertEquals(1, list.size());
        assertEquals(101L, list.get(0).getStudentId());
    }

    @Test
    void testCalculateAttendancePercentage() {
        Attendance presentDay = new Attendance();
        presentDay.setPresent(true);

        Attendance absentDay = new Attendance();
        absentDay.setPresent(false);

        when(attendanceRepository.findByStudentIdAndDateBetween(
                101L,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 5)
        )).thenReturn(Arrays.asList(presentDay, absentDay, presentDay));

        double percentage = attendanceService.calculateAttendancePercentage(
                101L,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 5)
        );

        assertEquals(66.66, percentage, 0.5); // ~2 out of 3 present
    }
    @Test
    void testGenerateMonthlyReport() {
        Attendance present = new Attendance();
        present.setPresent(true);
        Attendance absent = new Attendance();
        absent.setPresent(false);

        when(attendanceRepository.findByStudentIdAndDateBetween(
                101L,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 30)))
                .thenReturn(Arrays.asList(present, absent, present));

        String report = attendanceService.generateMonthlyReport(101L, 2025, 9);

        assertTrue(report.contains("Percentage: 66.67%"));
    }

}
