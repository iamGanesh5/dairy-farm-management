package com.dairy.farm.management.security;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =

            "mysecretkeymysecretkeymysecretkey12";

    private final Key key = Keys.hmacShaKeyFor(

            SECRET_KEY.getBytes()

    );

    public String generateToken(
            String username
    ) {

        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(

                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 10
                        )

                )

                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )

                .compact();

    }

    public String extractUsername(
            String token
    ) {

        return extractClaims(token)
                .getSubject();

    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(key)

                .build()

                .parseClaimsJws(token)

                .getBody();

    }

}