package com.bank.loans.infrastructure.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.bank.loans.application.service.LoanService;
import com.bank.loans.infrastructure.dto.request.LoanRequest;
import com.bank.loans.infrastructure.dto.response.LoanResponse;
import com.bank.loans.infrastructure.mapper.LoanDtoMapper;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping("/{userId}")
    public LoanResponse requestLoan(

            @PathVariable
            Long userId,


            @Valid
            @RequestBody
            LoanRequest request

    ){


        var loan =
                service.requestLoan(

                    LoanDtoMapper.toDomain(
                        request
                    ),

                    userId

                );

        return LoanDtoMapper
                .toResponse(
                    loan
                );

    }


    @GetMapping("/user/{userId}")
    public List<LoanResponse> userLoans(

            @PathVariable
            Long userId

    ){

        return service
                .getUserLoans(
                    userId
                )

                .stream()

                .map(
                    LoanDtoMapper::toResponse
                )

                .toList();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<LoanResponse> allLoans() {
        return service.getAll()
                .stream()
                .map(LoanDtoMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/approve")
    public LoanResponse approve(

            @PathVariable
            Long id
    ){

        return LoanDtoMapper
                .toResponse(

                    service.approveLoan(id)

                );

    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reject")
    public LoanResponse reject(

            @PathVariable
            Long id
    ){

        return LoanDtoMapper
                .toResponse(

                    service.rejectLoan(id)

                );

    }
}