package com.cts.sms.config;

import com.cts.sms.model.Role;
import com.cts.sms.model.User;
import com.cts.sms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDefaultAdmins(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

           
            List<User> defaultAdmins = List.of(
                    new User(null, "System Admin", "admin@sms.com", passwordEncoder.encode("admin123"), Role.ADMIN),
                    new User(null, "Admin Two", "admin2@sms.com", passwordEncoder.encode("admin234"), Role.ADMIN),
                    new User(null, "Admin Three", "admin3@sms.com", passwordEncoder.encode("admin345"), Role.ADMIN),
                    new User(null, "Admin Four", "admin4@sms.com", passwordEncoder.encode("admin456"), Role.ADMIN),
                    new User(null, "Admin Five", "admin5@sms.com", passwordEncoder.encode("admin567"), Role.ADMIN),
                    new User(null, "Admin Six", "admin6@sms.com", passwordEncoder.encode("admin678"), Role.ADMIN)
            );

            for (User admin : defaultAdmins) {
                if (userRepository.findByEmail(admin.getEmail()).isEmpty()) {
                    userRepository.save(admin);
                    System.out.println("✅ Default ADMIN created: " 
                            + admin.getEmail() + " / (password: " + "*****" + ")");
                }
            }
        };
    }
}
