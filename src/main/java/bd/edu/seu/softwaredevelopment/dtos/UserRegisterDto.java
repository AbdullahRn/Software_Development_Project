package bd.edu.seu.softwaredevelopment.dtos;

public class UserRegisterDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private UserDto.Role role; // "ADMIN" or "STAFF"

    public UserRegisterDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public UserDto.Role getRole() { return role; }
    public void setRole(UserDto.Role role) { this.role = role; }
}


