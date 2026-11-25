package project.dealdrop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.dealdrop.dto.AuthResponseDto;
import project.dealdrop.dto.RegisterRequestDto;
import project.dealdrop.entity.User;
import project.dealdrop.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock 
    private UserRepository userRepository;

    @Mock 
    private PasswordEncoder passwordEncoder;

    @InjectMocks 
    private AuthService authService;


    @Test 
    void register_newUser_withEncodedPass(){
        // arrange
        RegisterRequestDto dto=new RegisterRequestDto();
        dto.setEmail("pk@gmail.com");
        dto.setName("Pranjali");
        dto.setPassword("pk123");

        // act
        when(userRepository.existsByEmail("pk@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("pk123")).thenReturn("encodedPass");

        AuthResponseDto response=authService.register(dto);

        ArgumentCaptor<User> captor=ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved=captor.getValue();

        // assert
        assertEquals("pk@gmail.com",saved.getEmail());
        assertEquals("Pranjali",saved.getName());
        assertEquals("encodedPass",saved.getPassword());

        assertNotNull(response);
    }
}
