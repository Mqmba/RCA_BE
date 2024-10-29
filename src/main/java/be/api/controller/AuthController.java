package be.api.controller;

import be.api.dto.request.AuthRequestDTO;
import be.api.dto.request.UserRegistrationDTO;
import be.api.dto.response.AuthResponseDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.dto.response.UserResponseDTO;
import be.api.exception.ResourceConflictException;
import be.api.model.User;
import be.api.services.impl.AuthService;
import be.api.security.CustomUserDetailsService;
import be.api.security.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseData<?> authenticate(@RequestBody AuthRequestDTO request) {
        try {
                System.out.println("Attempting to authenticate user: " + request.getUsername());
                System.out.println("Using password: " + request.getPassword()); // Avoid logging this in production
                // Existing authentication logic...

            if (request.getUsername() == null || request.getPassword() == null ||
                    request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Username and password must not be null or empty.");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            return new ResponseData<>(
                    HttpStatus.OK.value(),
                    "Authentication successful",
                    new AuthResponseDTO(token)
            );
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        }
    }

    @PostMapping("/register/resident")
    public ResponseData<?> registerResident(@Valid @RequestBody UserRegistrationDTO userDto) {
        System.out.println("Registering resident: " + userDto.getUsername());
        return registerUser(userDto, User.UserRole.ROLE_RESIDENT, "Resident registration successful");
    }

    @PostMapping("/register/collector")
    public ResponseData<?> registerCollector(@Valid @RequestBody UserRegistrationDTO userDto) {
        return registerUser(userDto, User.UserRole.ROLE_COLLECTOR, "Collector registration successful");
    }

    @PostMapping("/register/recycling_depot")
    public ResponseData<?> registerRecyclingDepot(@Valid @RequestBody UserRegistrationDTO userDto) {
        return registerUser(userDto, User.UserRole.ROLE_RECYCLING_DEPOT, "Recycling depot registration successful");
    }

    private ResponseData<?> registerUser(UserRegistrationDTO userDto, User.UserRole role, String successMessage) {
        try {
            User user = authService.registerUser(userDto, role); // Use the AuthService for registration

            System.out.println("Registered user: " + user.getUsername() + " with encoded password: " + user.getPassword());

            UserResponseDTO userResponse = new UserResponseDTO();
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setRole(user.getRole().name());

            return new ResponseData<>(HttpStatus.CREATED.value(), successMessage, userResponse);
        } catch (ResourceConflictException e) {
            return new ResponseError(HttpStatus.CONFLICT.value(), e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Registration failed");
        }
    }
}