package com.bank.loans.application.service;

import com.bank.loans.application.ports.out.UserRepositoryPort;
import com.bank.loans.domain.enums.Role;
import com.bank.loans.domain.model.User;
import com.bank.loans.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Admin")
                .email("admin@test.com")
                .password("hashed_password")
                .role(Role.ADMIN)
                .build();
    }

    // ─── register ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar usuario: debe encriptar la contraseña antes de guardar")
    void register_shouldEncodePassword() {
        when(encoder.encode("plain123")).thenReturn("hashed_password");
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User input = User.builder()
                .name("Juan")
                .email("juan@test.com")
                .password("plain123")
                .role(Role.USER)
                .build();

        User result = userService.register(input);

        assertThat(result.getPassword()).isEqualTo("hashed_password");
        verify(encoder).encode("plain123");
        verify(repository).save(input);
    }

    @Test
    @DisplayName("Registrar usuario: rol null debe asignarse como USER por defecto")
    void register_shouldDefaultRoleToUser() {
        when(encoder.encode(any())).thenReturn("hashed");
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User input = User.builder()
                .name("Juan")
                .email("juan@test.com")
                .password("pass")
                .role(null)
                .build();

        User result = userService.register(input);

        assertThat(result.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Registrar usuario: rol ADMIN debe conservarse")
    void register_shouldKeepAdminRole() {
        when(encoder.encode(any())).thenReturn("hashed");
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User input = User.builder()
                .name("Admin")
                .email("admin@test.com")
                .password("pass")
                .role(Role.ADMIN)
                .build();

        User result = userService.register(input);

        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    // ─── login ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Login: credenciales correctas deben retornar token en password")
    void login_shouldReturnTokenOnSuccess() {
        when(repository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));
        when(encoder.matches("plain123", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken("admin@test.com")).thenReturn("jwt_token_123");

        User result = userService.login("admin@test.com", "plain123");

        assertThat(result.getPassword()).isEqualTo("jwt_token_123");
        verify(jwtService).generateToken("admin@test.com");
    }

    @Test
    @DisplayName("Login: usuario inexistente debe lanzar excepción")
    void login_shouldThrowWhenUserNotFound() {
        when(repository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("noexiste@test.com", "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no existe");

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login: contraseña incorrecta debe lanzar excepción")
    void login_shouldThrowWhenPasswordWrong() {
        when(repository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong", "hashed_password")).thenReturn(false);

        assertThatThrownBy(() -> userService.login("admin@test.com", "wrong"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Credenciales incorrectas");

        verify(jwtService, never()).generateToken(any());
    }

    // ─── findByEmail ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("FindByEmail: debe retornar el usuario si existe")
    void findByEmail_shouldReturnUser() {
        when(repository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("admin@test.com");

        assertThat(result.getEmail()).isEqualTo("admin@test.com");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("FindByEmail: debe lanzar excepción si no existe")
    void findByEmail_shouldThrowWhenNotFound() {
        when(repository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("noexiste@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}
