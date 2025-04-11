package com.shakhawat.authapp;


import com.shakhawat.authapp.entity.Role;
import com.shakhawat.authapp.entity.User;
import com.shakhawat.authapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User user = User.builder()
                        .firstname("Mollah")
                        .lastname("Sumon")
                        .email("user@example.com")
                        .password(passwordEncoder.encode("password"))
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
            }

            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = User.builder()
                        .firstname("Shakhawat")
                        .lastname("Mollah")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
