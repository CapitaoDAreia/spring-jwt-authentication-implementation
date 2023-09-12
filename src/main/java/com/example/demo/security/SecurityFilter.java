package com.example.demo.security;

import com.example.demo.domain.repository.UserRepository;
import com.example.demo.services.JwtServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    JwtServices jwtServices;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequestHeader(request);
        if(token != null){
            var tokenSubject = jwtServices.extractSubjectFromJwtToken(token); //retrieves the subject, in this case, the user email
            UserDetails user = userRepository.findByLogin(tokenSubject); //retrieve user from repository
            var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities()); //build an authentication based on retrieved user

            SecurityContextHolder.getContext().setAuthentication(authentication); //set authentication on request security context
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequestHeader(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null) return authorizationHeader.replace("Bearer ", "");
        return null;
    }
}
