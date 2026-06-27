package com.swiggy.user.service;

import com.swiggy.user.dto.*;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}