package com.example.jobportal.dto;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Data
public class RegisterUserDto {
    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

