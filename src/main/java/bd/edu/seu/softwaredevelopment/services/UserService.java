package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.LoginRequest;
import bd.edu.seu.softwaredevelopment.dtos.RegisterRequest;
import bd.edu.seu.softwaredevelopment.dtos.UserDto;
import bd.edu.seu.softwaredevelopment.interfaces.UserServiceInterface;
import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setAddress(registerRequest.getAddress());
        user.setCreatedAt(LocalDateTime.now());

        // FIXED: Robust role mapping to ensure it matches the Enum exactly
        if (registerRequest.getRole() != null) {
            user.setRole(User.Role.valueOf(registerRequest.getRole().name().toUpperCase()));
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> getUsersByRole(UserDto.Role role) {
        String targetRoleName = role.name();

        return userRepository.findAll().stream()
                // FIXED: Null check and case-insensitive comparison to ensure suppliers are found
                .filter(user -> user.getRole() != null &&
                        user.getRole().name().equalsIgnoreCase(targetRoleName))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User updateUser(String id, UserDto userDto) {
        User existingUser = getUserById(id);
        existingUser.setName(userDto.getName());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAddress(userDto.getAddress());

        if (userDto.getRole() != null) {
            existingUser.setRole(User.Role.valueOf(userDto.getRole().name().toUpperCase()));
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User login(String email, String password) {
        return null; // Handled by Security
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    @Override
    public User loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());

        if (user.getRole() != null) {
            dto.setRole(UserDto.Role.valueOf(user.getRole().name()));
        }

        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}