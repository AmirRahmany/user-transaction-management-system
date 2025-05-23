package com.dev.user_transaction_management_system.infrastructure.util.jwt;

import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthJwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthJwtTokenFilter.class);


    private final UserDetailsService userDetailsService;


    private final JwtTokenUtils jwtUtils;

    public AuthJwtTokenFilter(UserDetailsService userDetailsService, JwtTokenUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = parseJwtToken(request);

        try {
            if (token!=null && jwtUtils.isValidJwtToken(token)) {
                final String username = jwtUtils.getUsernameFromToken(token);
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
                logger.debug("Roles from Jwt's token: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            logger.error("Can not set user Authentication: {}",e.getMessage());
        }

        filterChain.doFilter(request,response);
    }

    private String parseJwtToken(HttpServletRequest request) {
        final String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter: {}", jwt);
        return jwt;
    }

}