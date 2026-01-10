package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.dtos.LoginRequest;
import bd.edu.seu.softwaredevelopment.dtos.RegisterRequest;
import bd.edu.seu.softwaredevelopment.dtos.UserDto;
import bd.edu.seu.softwaredevelopment.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    User registerUser(RegisterRequest registerRequest);

    Optional<User> findByEmail(String email);

    List<UserDto> getUsersByRole(UserDto.Role role);

    User loginUser(LoginRequest loginRequest);

    User getCurrentLoggedInUser();

    List<UserDto> getAllUsers();

    User getUserById(String id);

    User updateUser(String id, UserDto userDto);

    void deleteUser(String id);

    User login(String email, String password);
}


