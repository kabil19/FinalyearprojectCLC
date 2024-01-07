package com.appli.clcapi.userlogin.service.jwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "5626b0c635702f1cbdb3e0986db52b1a8ac139da2061975abfa05677fd3931d3";


    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public  <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims =extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
      return  Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*1000*60*30))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSigninKey() {
        byte[] keys = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return Objects.equals(userDetails.getUsername(), username) && isTokenNotExpired(token);
    }

    public boolean isTokenNotExpired(String token) {
        Date now = new Date(System.currentTimeMillis()*1000*60);
        return (extractTokenExpiration(token).before(now));
    }

    private Date extractTokenExpiration(String token){
        return  extractAllClaims(token).getExpiration();
    }



}
