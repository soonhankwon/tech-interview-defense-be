package dev.techmentordefensebe.common.security.filter;

import dev.techmentordefensebe.common.security.impl.UserDetailsImpl;
import dev.techmentordefensebe.common.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private static final String JWT_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String bearerToken = getAuthenticationHeaderValue(request);
        return bearerToken == null || !bearerToken.startsWith(JWT_PREFIX);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            setAuthenticationToContext(request, response);
        } catch (JwtException e) {
            request.setAttribute("exception", "invalid jwt");
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = createAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getAuthenticationHeaderValue(request);
        accessToken = accessToken.substring(JWT_PREFIX.length());

        Claims claims = jwtProvider.getClaims(accessToken);
        UserDetailsImpl userDetails = new UserDetailsImpl(claims);

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    private String getAuthenticationHeaderValue(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
