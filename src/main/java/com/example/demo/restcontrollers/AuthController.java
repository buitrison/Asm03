package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.LoginRequest;
import com.example.demo.dtos.request.RegisterRequest;
import com.example.demo.dtos.request.users.ResetPasswordRequest;
import com.example.demo.dtos.request.users.SendMailRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.User;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.service.UserService;
import com.example.demo.utils.CustomMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    // đăng kí
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            errorMessages,
                            HttpStatus.UNPROCESSABLE_ENTITY.name()
                    )
            );
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            "password not match",
                            HttpStatus.UNPROCESSABLE_ENTITY.name()
                    )
            );
        }

        User user = userService.createUser(registerRequest);
        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "dang ki thanh cong",
                        CustomMap.of(
                                "id", user.getId(),
                                "name", user.getName(),
                                "email", user.getEmail(),
                                "password", user.getPassword()
                        )
                ));

    }

    // đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(
                CustomMap.of(
                        "token", token
                )
        );
    }

    // gửi mail reset mật khẩu
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendEmail(@RequestBody SendMailRequest sendMail) throws InvalidKeyException {
        User existingUser = userService.findByEmail(sendMail.getEmail())
                .orElseThrow(
                        () -> new NotFoundException("Invalid Email")
                );

        String token = userService.generateToken(existingUser);
        String subject = "Reset Password of " + sendMail.getEmail();
        String text = "Click link: http://localhost:8080/Asm03/users/reset-password?token=" + token;
        userService.sendMail(sendMail.getEmail(), subject, text);
        return ResponseEntity.ok(new DataResponse(
                HttpStatus.OK.value(),
                "Send Email Successfuly!",
                null
        ));
    }


    // cập nhật mật khẩu
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
                                           @RequestBody ResetPasswordRequest request) {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new BadRequestException("Het han token");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password is not match");
        }
        String email = jwtTokenUtils.extractEmail(token);

        User user = userService.resetPassword(email, request.getPassword());
        return ResponseEntity.ok(new DataResponse(
                HttpStatus.OK.value(),
                "Reset password successfuly!",
                CustomMap.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "password", user.getPassword()
                )
        ));
    }
}
