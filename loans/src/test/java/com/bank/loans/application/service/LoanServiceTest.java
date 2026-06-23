package com.bank.loans.application.service;

import com.bank.loans.application.ports.out.LoanRepositoryPort;
import com.bank.loans.application.ports.out.UserRepositoryPort;
import com.bank.loans.domain.enums.LoanStatus;
import com.bank.loans.domain.model.Loan;
import com.bank.loans.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepositoryPort loanRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private LoanService loanService;

    private User user;
    private Loan loan;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Carlos Perez")
                .email("carlos@test.com")
                .build();

        loan = Loan.builder()
                .id(1L)
                .amount(new BigDecimal("5000000"))
                .termMonths(24)
                .status(LoanStatus.PENDING)
                .user(user)
                .build();
    }

    // ─── requestLoan ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Solicitar préstamo: debe guardar con estado PENDING")
    void requestLoan_shouldSaveWithPendingStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan input = Loan.builder()
                .amount(new BigDecimal("5000000"))
                .termMonths(24)
                .build();

        Loan result = loanService.requestLoan(input, 1L);

        assertThat(result.getStatus()).isEqualTo(LoanStatus.PENDING);
        assertThat(result.getUser()).isEqualTo(user);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Solicitar préstamo: debe lanzar excepción si el usuario no existe")
    void requestLoan_shouldThrowWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Loan input = Loan.builder()
                .amount(new BigDecimal("1000000"))
                .termMonths(12)
                .build();

        assertThatThrownBy(() -> loanService.requestLoan(input, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no existe");

        verify(loanRepository, never()).save(any());
    }

    // ─── approveLoan ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Aprobar préstamo: debe cambiar estado a APPROVED")
    void approveLoan_shouldChangeStatusToApproved() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.approveLoan(1L);

        assertThat(result.getStatus()).isEqualTo(LoanStatus.APPROVED);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    @DisplayName("Aprobar préstamo: debe lanzar excepción si el préstamo no existe")
    void approveLoan_shouldThrowWhenLoanNotFound() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.approveLoan(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Préstamo no encontrado");

        verify(loanRepository, never()).save(any());
    }

    // ─── rejectLoan ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Rechazar préstamo: debe cambiar estado a REJECTED")
    void rejectLoan_shouldChangeStatusToRejected() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.rejectLoan(1L);

        assertThat(result.getStatus()).isEqualTo(LoanStatus.REJECTED);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    @DisplayName("Rechazar préstamo: debe lanzar excepción si el préstamo no existe")
    void rejectLoan_shouldThrowWhenLoanNotFound() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.rejectLoan(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Préstamo no encontrado");
    }

    // ─── getUserLoans ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Consultar préstamos: debe retornar lista del usuario")
    void getUserLoans_shouldReturnUserLoans() {
        when(loanRepository.findByUserId(1L)).thenReturn(List.of(loan));

        List<Loan> result = loanService.getUserLoans(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualByComparingTo("5000000");
        verify(loanRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Consultar préstamos: usuario sin préstamos retorna lista vacía")
    void getUserLoans_shouldReturnEmptyListWhenNone() {
        when(loanRepository.findByUserId(2L)).thenReturn(List.of());

        List<Loan> result = loanService.getUserLoans(2L);

        assertThat(result).isEmpty();
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Obtener todos: debe retornar todos los préstamos")
    void getAll_shouldReturnAllLoans() {
        Loan second = Loan.builder().id(2L).status(LoanStatus.APPROVED).build();
        when(loanRepository.findAll()).thenReturn(List.of(loan, second));

        List<Loan> result = loanService.getAll();

        assertThat(result).hasSize(2);
        verify(loanRepository, times(1)).findAll();
    }
}
