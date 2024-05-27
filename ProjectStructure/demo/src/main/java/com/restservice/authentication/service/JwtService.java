package com.restservice.authentication.service;

import com.restservice.authentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.function.Function;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private String SECRET_KEY = "546b4f85aee3108f1ff4b5a3e7d63435d1577c3705adf33b0ef5d7e63f7dedf3";

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user){
        System.out.println("Am intrat in isValid");
        String username = extractUsername(token);
        System.out.println("Username from token: " + username);
        System.out.println("Username from UserDetails: " + user.getUsername());
        boolean usernameMatches = username.equals(user.getUsername());
        System.out.println("Username matches: " + usernameMatches);
        return usernameMatches && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        System.out.println("Am intrat in isTokenExpired");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        System.out.println("Am intrat in extractExpiration");
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        System.out.println("Generating token for user: " + user.getUsername());
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSignInKey())
                .compact();
        return token;
    }
}
