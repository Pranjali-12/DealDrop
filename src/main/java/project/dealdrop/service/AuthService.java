package project.dealdrop.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import project.dealdrop.dto.AuthResponseDto;
import project.dealdrop.dto.LoginRequestDto;
import project.dealdrop.dto.LoginResponseDto;
import project.dealdrop.dto.RegisterRequestDto;
import project.dealdrop.entity.User;
import project.dealdrop.repository.UserRepository;
import project.dealdrop.security.JwtService;
import project.dealdrop.type.UserRole;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthResponseDto register(RegisterRequestDto registerRequestDto){
        if(userRepository.existsByEmail(registerRequestDto.getEmail())){
            throw new RuntimeException("Email already exist");
        }

        User user=new User();
        user.setName(registerRequestDto.getName());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());

        if(registerRequestDto.getRole()==UserRole.SELLER){
            user.setRole(registerRequestDto.getRole());
        }

        userRepository.save(user);

        return new AuthResponseDto(true,"User registered successfully");
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        // User user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()->new RuntimeException("Email not exist"));

        // if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
        //     throw new RuntimeException("Invalid credentials");
        // }

        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        User user=(User) authentication.getPrincipal();

        String token=jwtService.generateAccessToken(user);

        return new LoginResponseDto(user.getId(),token);
    }


}
