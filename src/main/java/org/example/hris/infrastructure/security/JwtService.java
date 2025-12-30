package org.example.hris.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        // Menyimpan roles di dalam token
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        extraClaims.put("roles", roles);

        return Jwts
                .builder()
                .claims(extraClaims) // Menggunakan .claims() lebih modern
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey()) // .signWith() tidak perlu Algoritma, krn key sudah menyediakannya
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * ======================================================================
     * INI ADALAH BAGIAN YANG DIPERBARUI (UNTUK JJWT 0.12.x)
     * ======================================================================
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser() // <-- Mengganti parserBuilder() menjadi parser()
                .verifyWith(getSignInKey()) // <-- Mengganti setSigningKey() menjadi verifyWith()
                .build()
                .parseSignedClaims(token) // <-- Mengganti parseClaimsJws() menjadi parseSignedClaims()
                .getPayload(); // <-- Mengganti getBody() menjadi getPayload()
    }

    /**
     * ======================================================================
     * INI JUGA DIPERBARUI (UNTUK JJWT 0.12.x)
     * ======================================================================
     */
    private SecretKey getSignInKey() { // <-- Mengembalikan SecretKey, bukan Key
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}