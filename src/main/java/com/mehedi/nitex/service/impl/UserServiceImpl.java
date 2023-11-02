package com.mehedi.nitex.service.impl;

import com.mehedi.nitex.model.User;
import com.mehedi.nitex.model.UserPrincipal;
import com.mehedi.nitex.repository.UserRepository;
import com.mehedi.nitex.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import static com.mehedi.nitex.enumration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // load user by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error("No user found by username" + username);
            throw new UsernameNotFoundException("No user found by username" + username);
        } else {
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info("Found user by username" + username);
            return userPrincipal;
        }
    }

    @Override
    public User register(String fullName, String username, String email, String password) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setDateRegistered(new Date());
        user.setPassword(encodePassword(password));
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        userRepository.save(user);
        return user;
    }


    // find user by username
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    // find user by email
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // encoded password
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
