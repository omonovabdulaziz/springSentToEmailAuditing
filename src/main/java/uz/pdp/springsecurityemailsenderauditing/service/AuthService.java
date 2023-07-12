package uz.pdp.springsecurityemailsenderauditing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurityemailsenderauditing.entity.User;
import uz.pdp.springsecurityemailsenderauditing.entity.enums.RoleName;
import uz.pdp.springsecurityemailsenderauditing.payload.ApiResponse;
import uz.pdp.springsecurityemailsenderauditing.payload.LoginDTO;
import uz.pdp.springsecurityemailsenderauditing.payload.RegisterDTO;
import uz.pdp.springsecurityemailsenderauditing.repository.RoleRepository;
import uz.pdp.springsecurityemailsenderauditing.repository.UserRepository;
import uz.pdp.springsecurityemailsenderauditing.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDTO registerDTO) {
        boolean existsByEmail = userRepository.existsByEmail(registerDTO.getEmail());
        if (existsByEmail)
            return new ApiResponse("bunday emailli user mavjud", false);

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        user.setEmailCode(UUID.randomUUID().toString());
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);


        //emailga yuborish metodini chaqiramiz
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("emailni  tasdiqlang", true);

    }


    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Test@pdp.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Tasdiqlash kodi");
            simpleMailMessage.setText("<a href = 'http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "' >Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("tasdiqlandi", true);
        }

        return new ApiResponse("allqachon tasdiqlang", true);

    }

    public ApiResponse login(LoginDTO loginDTO) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDTO.getUsername(), user.getRoles());
            return new ApiResponse("Token " , true , token);
        } catch (Exception e) {
         return new ApiResponse("parol yoki login hato" , false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "topilmadi"));
    }
}
