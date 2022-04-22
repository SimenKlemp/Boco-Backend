package edu.ntnu.idatt2106.boco.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenComponent
{
    private String jwtSecret = "TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig";

    public String generateToken(long userId, String role) throws UnsupportedEncodingException
    {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes("UTF-8"));

        ArrayList<String> roles = new ArrayList<>();
        roles.add(role);

        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("userId", userId);
        claims.put("authorities", roles);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(String.valueOf(userId))
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .signWith(key)
                .compact();
    }

    public boolean haveAccessTo (long userId)
    {
        return isThisUser(userId) || isAdmin();
    }

    public boolean isThisUser (long userId)
    {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()) == userId;
    }

    public boolean isAdmin ()
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
}
