package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.User;
import com.xeno.xenocrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        if (user != null) {
            System.out.println("DB PASSWORD = " + user.getPassword());

            boolean matched =
                    passwordEncoder.matches(
                            password,
                            user.getPassword());

            System.out.println("MATCHED = " + matched);

            if (matched) {
                return user;
            }
        }

        return null;
    }
}