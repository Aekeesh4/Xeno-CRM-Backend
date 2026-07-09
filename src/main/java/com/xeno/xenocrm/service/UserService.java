package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.User;
import com.xeno.xenocrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    public User saveUser(User user) {

        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email);

        System.out.println("EMAIL = " + email);

        if(user == null){
            System.out.println("USER NOT FOUND");
            return null;
        }

        System.out.println("DB PASSWORD = " + user.getPassword());

        boolean matched =
                passwordEncoder.matches(
                        password,
                        user.getPassword());

        System.out.println("MATCHED = " + matched);

        if(matched){
            return user;
        }

        return null;
    }
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "User not found";
        }

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String resetLink =
                "http://localhost:5173/reset-password?token=" + token;

        String body =
                "Hi " + user.getName() + ",\n\n"
                        + "Click the link below to reset your password:\n\n"
                        + resetLink
                        + "\n\nThis link will expire in 15 minutes.";

        emailService.sendEmail(
                user.getEmail(),
                "Reset Your XenoCRM Password",
                body
        );

        return "Password reset link sent successfully.";
    }

    public String resetPassword(String token, String newPassword) {

        Optional<User> optionalUser =
                userRepository.findByResetToken(token);

        if (optionalUser.isEmpty()) {
            return "Invalid token";
        }

        User user = optionalUser.get();

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return "Reset token has expired";
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);

        return "Password reset successful.";
    }
}