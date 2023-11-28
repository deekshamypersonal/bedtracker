package com.bedmanagement.bedtracker.common;

import com.bedmanagement.bedtracker.exception.ErrorMessageConstant;
import com.bedmanagement.bedtracker.exception.UserServiceException;
import com.bedmanagement.bedtracker.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class Utility {
    public static boolean validateFiles(MultipartFile file) {

        if(file.getSize() > Constants.ALLOWED_FILE_SIZE) {
            throw new UserServiceException(ErrorMessageConstant.INVALID_FILE_SIZE);
        }

        String extension = FilenameUtils.getExtension(
                file.getOriginalFilename());
        if (!isSupportedExtension(extension))
            throw new UserServiceException(ErrorMessageConstant.HOSPITAL_NOT_FOUND);
        return true;
    }
    private static boolean isSupportedExtension(String extension) {
        return extension != null && (
                extension.equals("png")
                        || extension.equals("jpg")
                        || extension.equals("jpeg")
                        || extension.equals("pdf"));
    }


    public static String generateToken(String userName){
        Instant now = Instant.now();

        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.getTokenSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static boolean isTokenValid(String token) {
        boolean returnValue = true;
try{
        Claims claims= Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String user=claims.getSubject();
        } catch (ExpiredJwtException ex) {
            returnValue = false;
        }

        return returnValue;
    }


}
