package project.dealdrop.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import project.dealdrop.dto.RequestSellerDto;
import project.dealdrop.entity.User;
import project.dealdrop.repository.UserRepository;
import project.dealdrop.security.JwtService;
import project.dealdrop.type.SellerRequest;
import project.dealdrop.type.UserRole;
import project.dealdrop.exception.DuplicateResourceException;
import project.dealdrop.exception.ResourceNotFoundException;
import project.dealdrop.exception.BadRequestException;
import org.springframework.security.access.AccessDeniedException;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exist");
        }

        User user = new User();
        user.setName(registerRequestDto.getName());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());

        if (registerRequestDto.getRole() == UserRole.SELLER || registerRequestDto.getRole() == UserRole.ADMIN) {
            user.setRole(registerRequestDto.getRole());
        }

        userRepository.save(user);

        return new AuthResponseDto(true, "User registered successfully");
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // User
        // user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()->new
        // RuntimeException("Email not exist"));

        // if(!passwordEncoder.matches(loginRequestDto.getPassword(),
        // user.getPassword())){
        // throw new RuntimeException("Invalid credentials");
        // }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateAccessToken(user);

        return new LoginResponseDto(user.getId(), token);
    }

    public ResponseEntity<String> requestForSeller(RequestSellerDto requestSellerDto, User user) {
        if (user.getRole() == UserRole.SELLER) {
            throw new DuplicateResourceException("Already a seller");
        }

        if (user.getSellerRequest() == SellerRequest.PENDING) {
            throw new DuplicateResourceException("Seller request is already pending");
        }

        user.setSellerCategory(requestSellerDto.getSellerCategory());
        user.setSellerReason(requestSellerDto.getSellerReason());
        user.setSellerRequest(SellerRequest.PENDING);

        userRepository.save(user);

        return ResponseEntity.ok("Request has been sent");
    }

    public List<User> getRequestedSeller(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Accessed Denied");
        }

        List<User> list = userRepository.findBysellerRequest(SellerRequest.PENDING);

        return list;
    }

    public String changeRequestStatus(Long userId, SellerRequest sellerRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found: " + userId));

        if (user.getSellerRequest() != SellerRequest.PENDING) {
            throw new BadRequestException("No pending seller request found for this user");
        }

        if (sellerRequest == null ||
                (sellerRequest != SellerRequest.APPROVED &&
                        sellerRequest != SellerRequest.REJECTED)) {

            throw new BadRequestException("Invalid seller request status");
        }

        if (sellerRequest == SellerRequest.APPROVED) {
            user.setSellerRequest(sellerRequest);
            user.setRole(UserRole.SELLER);
        }

        else if (sellerRequest == SellerRequest.REJECTED) {
            user.setSellerRequest(sellerRequest);
        }

        else {
            throw new IllegalArgumentException("Seller Request is invalid");
        }

        userRepository.save(user);

        return "Request Status has been changed";
    }
}
