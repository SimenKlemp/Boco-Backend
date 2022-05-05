package edu.ntnu.idatt2106.boco.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenFilter extends OncePerRequestFilter
{
    private final String HEADER = "Authorization";

    private final String jwtSecret = "TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig TODO: gjor_denne_hemmelig";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // expects JWT in the header
            String authenticationHeader = request.getHeader(HEADER);
            final String PREFIX = "Bearer ";

            // check Authorization header exists
            if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            {
                SecurityContextHolder.clearContext();
            }
            else
            {
                // get token and claims
                String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);

                // perform necessary checks
                if (claims.getBody().get("authorities") != null)
                {
                    // setup Spring authentication
                    List<String> authorities = (List) claims.getBody().get("authorities");
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                else
                {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e)
        {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}
