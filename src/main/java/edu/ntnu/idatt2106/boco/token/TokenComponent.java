package edu.ntnu.idatt2106.boco.token;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenComponent
{
    private String jwtSecret = "TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig";

    public String generateToken(long userId, String role) throws UnsupportedEncodingException
    {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));



        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("userId", userId);
        claims.put("authorities", role);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(String.valueOf(userId))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(key)
                .compact();
    }

    public static boolean isThisUser (long userId)
    {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()) == userId;
    }

    public static boolean isAdmin ()
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
}
