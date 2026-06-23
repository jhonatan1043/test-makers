package com.bank.loans.infrastructure.security;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
            "12345678901234567890123456789012345678901234567890";

    private SecretKey getKey(){

        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );

    }

    public String generateToken(
            String email
    ){


        return Jwts.builder()

                .subject(email)

                .issuedAt(
                        new Date()
                )

                .expiration(
                        new Date(
                            System.currentTimeMillis()
                            +
                            1000 * 60 * 60
                        )
                )

                .signWith(
                        getKey()
                )

                .compact();


    }

    public String getEmail(
            String token
    ){
        return Jwts.parser()

                .verifyWith(
                        getKey()
                )

                .build()

                .parseSignedClaims(
                        token
                )

                .getPayload()

                .getSubject();

    }

}