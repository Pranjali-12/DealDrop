package project.dealdrop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import project.dealdrop.dto.RequestSellerDto;
import project.dealdrop.dto.changeStatusDto;
import project.dealdrop.entity.User;
import project.dealdrop.security.AuthenticationService;
import project.dealdrop.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }

    @PreAuthorize("hasRole('BIDDER')")
    @PostMapping("/request-seller")
    public ResponseEntity<String> requestForSeller(@RequestBody RequestSellerDto requestSellerDto){
        User user=authenticationService.getCurrentUser();
        return authService.requestForSeller(requestSellerDto,user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getrequestedseller")
    public ResponseEntity<List<User>> getRequestedSellers(){
        User user=authenticationService.getCurrentUser();
        return ResponseEntity.ok(authService.getRequestedSeller(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changerequeststatus/{userId}")
    public ResponseEntity<String> changeStatus(@PathVariable Long userId,@RequestBody changeStatusDto changeStatusDto){
        return ResponseEntity.ok(authService.changeRequestStatus(userId,changeStatusDto.getSellerRequest()));
    }
}
