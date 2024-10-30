package be.api.initializer;

import be.api.model.Admin;
import be.api.model.User;
import be.api.repository.IAdminRepository;
import be.api.repository.IUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class AdminInitializer implements ApplicationRunner {
    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(IUserRepository userRepository, IAdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!adminRepository.existsById(1)) { // Assuming Admin ID starts from 1
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.UserRole.ROLE_ADMIN);
            adminUser.setIsActive(true);
            // Set other required fields, e.g., roles or any additional properties
            userRepository.save(adminUser);

            Admin admin = new Admin();
            admin.setUser(adminUser);
            adminRepository.save(admin);
        }
    }
}
