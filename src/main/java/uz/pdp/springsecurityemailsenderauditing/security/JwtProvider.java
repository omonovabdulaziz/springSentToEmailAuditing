package uz.pdp.springsecurityemailsenderauditing.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurityemailsenderauditing.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private String maxfiySuz  = "buTokenniMaxfiySuziHechKimBilmasin";
    private final long expiredTime = 1000*60*60*24;
    public String generateToken(String username , Set<Role> roles){
        Date date = new Date(System.currentTimeMillis() + expiredTime);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, maxfiySuz)
                .compact();
        return token;
    }


    public String getEmailFromToken(String token){
            try {
                String email = Jwts
                        .parser()
                        .setSigningKey(maxfiySuz)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                return email;
            }catch (Exception e){
                return null;
            }
    }
}
