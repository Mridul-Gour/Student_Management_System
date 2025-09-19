package com.cts.sms.tests;

import com.cts.sms.dto.UserRequestDTO;
import com.cts.sms.dto.UserResponseDTO;
import com.cts.sms.model.Role;
import com.cts.sms.model.User;
import com.cts.sms.repository.UserRepository;
import com.cts.sms.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "Peeyush", "peeyush@test.com", "hashedPass", Role.STUDENT);
    }

    @Test
    void testSaveUser() {
        UserRequestDTO request = new UserRequestDTO("Peeyush", "peeyush@test.com", "Peeyush@123", Role.STUDENT);

        when(passwordEncoder.encode(anyString())).thenReturn("hashedPass");
        when(repo.save(any(User.class))).thenReturn(mockUser);

        UserResponseDTO saved = service.saveUser(request);

        assertEquals("Peeyush", saved.getName());
        assertEquals("peeyush@test.com", saved.getEmail());
        assertEquals(Role.STUDENT, saved.getRole());
        verify(passwordEncoder, times(1)).encode("Peeyush@123");
        verify(repo, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(repo.findAll()).thenReturn(List.of(mockUser));

        List<UserResponseDTO> users = service.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Peeyush", users.get(0).getName());
        assertEquals(Role.STUDENT, users.get(0).getRole());
    }

    @Test
    void testGetUserByEmail() {
        when(repo.findByEmail("peeyush@test.com")).thenReturn(Optional.of(mockUser));

        Optional<UserResponseDTO> result = service.getUserByEmail("peeyush@test.com");

        assertTrue(result.isPresent());
        assertEquals("Peeyush", result.get().getName());
        assertEquals(Role.STUDENT, result.get().getRole());
    }
}
