package com.xeno.xenocrm.controller;

import com.xeno.xenocrm.entity.User;
import com.xeno.xenocrm.service.UserService;
import com.xeno.xenocrm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xeno.xenocrm.dto.ForgotPasswordRequest;
import com.xeno.xenocrm.dto.ResetPasswordRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(
            @RequestBody User user) {

        User saved = userService.saveUser(user);

        System.out.println("REGISTERED = " + saved.getEmail());

        return saved;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody User user) {

        User loggedInUser =
                userService.login(
                        user.getEmail(),
                        user.getPassword()
                );

        Map<String, Object> response =
                new HashMap<>();

        if (loggedInUser != null) {

            String token =
                    jwtUtil.generateToken(
                            loggedInUser.getEmail()
                    );

            response.put("message", "Login Successful");
            response.put("token", token);
            response.put("user", loggedInUser);

        } else {

            response.put("message", "Invalid Credentials");

        }

        return response;
    }
    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        Map<String, String> response = new HashMap<>();

        String message =
                userService.forgotPassword(
                        request.getEmail()
                );

        response.put("message", message);

        return response;
    }
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        Map<String, String> response = new HashMap<>();

        String message =
                userService.resetPassword(
                        request.getToken(),
                        request.getPassword()
                );

        response.put("message", message);

        return response;
    }
}