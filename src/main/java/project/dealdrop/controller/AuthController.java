package project.dealdrop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.AuthResponseDto;
import project.dealdrop.dto.LoginRequestDto;
import project.dealdrop.dto.LoginResponseDto;
import project.dealdrop.dto.RegisterRequestDto;
import project.dealdrop.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        return authService.register(registerRequestDto);
    }

    @GetMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }
}
