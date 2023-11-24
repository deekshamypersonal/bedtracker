package com.bedmanagement.bedtracker.security;

import com.bedmanagement.bedtracker.io.entity.User;
import com.bedmanagement.bedtracker.io.repository.UserRepository;
import com.bedmanagement.bedtracker.user.service.impl.UserServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    UserRepository userRepository;
    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
        super(authManager);
        this.userRepository=userRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
//        Cookie[] cr= request.getCookies();
//        String token= Arrays.stream(request.getCookies())
//                .filter(c -> c.getName().equals("Authorization"))
//                .findFirst()
//                .map(Cookie::getValue)
//                .orElse(null);
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {

            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        }

        Claims claims= Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String user=claims.getSubject();

       // UserRepository userRepository=(UserRepository)SpringApplicationContext.getBean("userRepository");

        User userEntity = userRepository.findByEmail(user);
        List<GrantedAuthority> authorities=UserServiceImpl.getGrantedAuthorities(userEntity);

        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }

        return null;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.getTokenSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }


}



