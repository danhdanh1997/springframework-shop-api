package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.RefreshToken;
import com.xuandanh.springbootshop.exception.TokenRefreshException;
import com.xuandanh.springbootshop.jwt.JwtUtils;
import com.xuandanh.springbootshop.payload.JwtRefreshResponse;
import com.xuandanh.springbootshop.payload.LoginRequest;
import com.xuandanh.springbootshop.payload.TokenRefreshRequest;
import com.xuandanh.springbootshop.payload.TokenRefreshResponse;
import com.xuandanh.springbootshop.service.RefreshTokenService;
import com.xuandanh.springbootshop.service.UserDetailsImpl;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenResources {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private static final Logger logger = Logger.getLogger(AuthResources.class);
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public JwtUtils getJwtUtils() {
        return jwtUtils;
    }

    public RefreshTokenService getRefreshTokenService() {
        return refreshTokenService;
    }

    public RefreshTokenResources(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService){
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        logger.info("Login User returned [API[: " + userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtRefreshResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {

        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
