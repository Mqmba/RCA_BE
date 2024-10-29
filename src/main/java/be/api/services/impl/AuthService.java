package be.api.services.impl;

import be.api.dto.request.UserRegistrationDTO;
import be.api.exception.ResourceConflictException; // Custom exception for conflict scenarios
import be.api.model.User;
import be.api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerUser(UserRegistrationDTO userDto, User.UserRole role) {
        try {
            // Check for existing user by email, username, or phone number
            if (userRepository.findByEmail(userDto.getEmail()) != null) {
                throw new ResourceConflictException("Email is already in use.");
            }

            if (userRepository.findByUsername(userDto.getUsername()) != null) {
                throw new ResourceConflictException("Username is already in use.");
            }

            if (userRepository.findByPhoneNumber(userDto.getPhoneNumber()) != null) {
                throw new ResourceConflictException("Phone number is already in use.");
            }


            System.out.println("Encoded password: " + bCryptPasswordEncoder.encode(userDto.getPassword()));

            // If no existing user, proceed to save
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setRole(role);
            user.setEmailConfirmed(true);
            user.setIsActive(true);

            return userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            throw e;
        }
    }
}
