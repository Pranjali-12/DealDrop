package project.dealdrop.service;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import project.dealdrop.dto.AuthResponseDto;
import project.dealdrop.dto.LoginRequestDto;
import project.dealdrop.dto.RegisterRequestDto;
import project.dealdrop.entity.User;
import project.dealdrop.repository.UserRepository;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto registerRequestDto){
        if(userRepository.existsByEmail(registerRequestDto.getEmail())){
            throw new RuntimeException("Email already exist");
        }

        User user=new User();
        user.setName(registerRequestDto.getName());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());

        userRepository.save(user);

        return new AuthResponseDto(true,"User registered successfully");
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication=authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail, loginRequestDto.getPassword())
        );

        User user=(User) authentication.getPrincipal();
        // if(!userRepository.existsByEmail(loginRequestDto.getEmail())){
        //     throw new RuntimeException("Email not exist");
        // }

        // User user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow();

        // return new AuthResponseDto(true,"User logged in successfully");
    }


}
