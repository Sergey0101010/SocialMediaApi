package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.dto.AuthenticationRequest;
import com.sergey.socialmediaapi.domain.dto.AuthenticationResponse;
import com.sergey.socialmediaapi.domain.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse  authenticate(AuthenticationRequest request);
}
