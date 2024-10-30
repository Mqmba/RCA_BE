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
import be.api.services.impl.TokenBlackListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthService authService;
    private final TokenBlackListService tokenBlackListService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseData<?> authenticate(@RequestBody AuthRequestDTO request) {
        try {
            logger.info("Attempting to authenticate user: {}", request.getUsername());

            if (request.getUsername() == null || request.getPassword() == null ||
                    request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Username and password must not be null or empty.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new ResponseData<>(HttpStatus.OK.value(),
                    "Authentication successful",
                    new AuthResponseDTO(token, roles)
            );
        } catch (BadCredentialsException e) {
            logger.warn("Authentication error: Invalid username or password");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password");
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        }
    }

    @PostMapping("/logout")
    public ResponseData<?> logout(@RequestBody AuthRequestDTO request) {
        logger.info("Attempting logout for username: {}", request.getUsername());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Logout failed: User not authenticated");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "User not authenticated");
        }

        String authenticatedUsername = authentication.getName();
        logger.info("Authenticated username retrieved: {}", authenticatedUsername);

        // Validate the requested username
        if (!authenticatedUsername.equals(request.getUsername())) {
            logger.warn("Logout failed: Provided username {} does not match authenticated username {}", request.getUsername(), authenticatedUsername);
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Username does not match authenticated user");
        }

        // Check if the token has already been blacklisted
        String token = (String) authentication.getCredentials();
        if (tokenBlackListService.isTokenBlacklisted(token)) {
            logger.warn("Logout failed: Token has already been blacklisted");
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Token has already been blacklisted");
        }

        // Blacklist the token for security
        tokenBlackListService.setTokenBlacklist(token, 3600);

        logger.info("Logout successful for username: {}", authenticatedUsername);
        return new ResponseData<>(HttpStatus.OK.value(), "Logout successful", authenticatedUsername);
    }

    // Registration methods remain unchanged
    @PostMapping("/register/resident")
    public ResponseData<?> registerResident(@Valid @RequestBody UserRegistrationDTO userDto) {
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
            User user = authService.registerUser(userDto, role);
            UserResponseDTO userResponse = new UserResponseDTO();
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            userResponse.setPhoneNumber(user.getPhoneNumber());
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