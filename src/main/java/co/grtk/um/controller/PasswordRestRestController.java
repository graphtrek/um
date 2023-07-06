package co.grtk.um.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public class PasswordRestRestController {

    @PostMapping("/api/forgotPassword")
    public ResponseEntity<String> passwordResetEmail(@RequestBody String email) {
        return new ResponseEntity<>("passwordResetToken", HttpStatus.OK);
    }

    @GetMapping("/api/resendPasswordResetToken")
    public ResponseEntity<String> resendPasswordToken(@RequestParam("token") String oldToken, final HttpServletRequest request) {
        return new ResponseEntity<>("passwordResetToken", HttpStatus.OK);
    }

}
