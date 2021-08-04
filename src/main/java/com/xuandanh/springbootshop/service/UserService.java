package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.User;
import com.xuandanh.springbootshop.jwt.AuthEntryPointJwt;
import com.xuandanh.springbootshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    public static long getExpireTokenAfterMinutes() {
        return EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String forgotPassword(String email) {

        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByEmail(email));

        if (userOptional.isEmpty()) {
            return "Invalid email id.";
        }

        User user = userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user = userRepository.save(user);

        return user.getToken();
    }

    /**
     * Generate unique token. You may add multiple parameters to create a strong
     * token.
     *
     * @return unique token
     */
    private String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    /**
     * Check whether the created token expired or not.
     *
     * @param tokenCreationDate
     * @return true or false
     */
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public String resetPassword(String token, String password) {

        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByToken(token));

        if (userOptional.isEmpty()) {
            return "Invalid token.";
        }

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";

        }

        User user = userOptional.get();
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);

        return "Your password successfully updated.";
    }

    public User findOne(Long id){
        Optional<User>user = userRepository.findById(id);
        try{
            if(user.isPresent()){
                return user.get();
            }
        }catch (Exception exception){
            logger.error(exception.getMessage());
        }
        return user.orElseThrow(()->new UsernameNotFoundException("user "+id+" not exit"));
    }
}
