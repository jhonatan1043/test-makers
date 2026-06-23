package com.bank.loans.infrastructure.security;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.bank.loans.application.ports.out.UserRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepositoryPort userRepository;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        String header =
                request.getHeader(
                        "Authorization"
                );

        if(
                header == null
                ||
                !header.startsWith("Bearer ")
        ){


            filterChain.doFilter(
                    request,
                    response
            );


            return;

        }

        String token =
                header.substring(7);

        String email =
                jwtService.getEmail(
                        token
                );

        var user =
                userRepository
                    .findByEmail(
                            email
                    );

        if(
                user.isPresent()
                &&
                SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    == null
        ){

            var authorities =
                    java.util.List.of(

                        new SimpleGrantedAuthority(

                            "ROLE_"
                            +
                            user.get()
                                .getRole()
                                .name()

                        )

                    );

            var authentication =

                    new UsernamePasswordAuthenticationToken(

                        user.get(),

                        null,

                        authorities

                    );

            SecurityContextHolder

                    .getContext()

                    .setAuthentication(

                            authentication

                    );


        }

        filterChain.doFilter(

                request,

                response

        );

    }
}