package uz.pdp.springsecurityemailsenderauditing.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.springsecurityemailsenderauditing.security.JwtFilter;
import uz.pdp.springsecurityemailsenderauditing.service.AuthService;

import java.util.Properties;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthService authService;
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/register", "/api/auth/verifyEmail" , "/api/auth/login").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }
}
