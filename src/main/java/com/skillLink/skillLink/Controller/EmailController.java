package com.skillLink.skillLink.Controller;

import com.skillLink.skillLink.Service.EmailService;
import com.skillLink.skillLink.Service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/email")
@Tag(name = "Email API", description = "API for managing email operations")
public class EmailController {
    @Autowired
    private EmailService emailService;
    RedisService redisService;


    @GetMapping("/code")
    @Operation(summary = "to verify code ")
    public ResponseEntity<?> VerifyOTP(@RequestBody @NotEmpty String email, String otp) {
        Boolean otpVerified = redisService.verifyCode(email,otp);
        if (!otpVerified) {
            return ResponseEntity
                    .badRequest()
                    .body("failed to verify otp");
        }
        return  ResponseEntity
                .ok("otp verified successfully");
    }
}
