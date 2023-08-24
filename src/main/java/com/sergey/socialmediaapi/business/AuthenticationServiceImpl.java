package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.config.JwtService;
import com.sergey.socialmediaapi.domain.dto.AuthenticationRequest;
import com.sergey.socialmediaapi.domain.dto.AuthenticationResponse;
import com.sergey.socialmediaapi.domain.dto.RegisterRequest;
import com.sergey.socialmediaapi.domain.model.Role;
import com.sergey.socialmediaapi.domain.model.User;
import com.sergey.socialmediaapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {

            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            log.info("USER ALREADY EXISTS");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }



    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.info("NO Such user for auth");
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
