package com.example.mega_city_cab.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtUitil {
    @Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain Chain)
            throws ServletException, IOException {


                String token = request.getHeader("Authorization");
                if (token != null && token.startsWith("Bearer")) {

                    token = token.substring(7);
                    String email= jwtUtil.extractUsername(token);
                    if (email != null && SecurityContextHolder.getContext().getAuthentication()== null) {

                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        if (jwtUtil.validateToken(token, userDetails)) {

                            String role = "ROLE_"+jwtUtil.extractRole(token);
                            SecurityContextHolder.getContext().setAuthentication(

                                new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(role)))
                            );

                        }
                    }



                }
        Chain.doFilter(request, response);
    }
}
    
}
