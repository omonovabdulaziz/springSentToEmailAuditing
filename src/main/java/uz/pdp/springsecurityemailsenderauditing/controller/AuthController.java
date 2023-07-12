package uz.pdp.springsecurityemailsenderauditing.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurityemailsenderauditing.payload.ApiResponse;
import uz.pdp.springsecurityemailsenderauditing.payload.LoginDTO;
import uz.pdp.springsecurityemailsenderauditing.payload.RegisterDTO;
import uz.pdp.springsecurityemailsenderauditing.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterDTO registerDTO) {
        ApiResponse apiResponse = authService.registerUser(registerDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);

    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse apiResponse = authService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO ) {
        ApiResponse apiResponse = authService.login(loginDTO);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }

}
