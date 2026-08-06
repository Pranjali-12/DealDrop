package project.dealdrop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.dealdrop.type.UserRole;

@Data
public class RegisterRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8,max = 20)
    private String password;

    private UserRole role;
}
