package com.mehedi.nitex.service.impl;

import com.mehedi.nitex.exceptions.model.EmailExistException;
import com.mehedi.nitex.exceptions.model.UserNotFoundException;
import com.mehedi.nitex.exceptions.model.UsernameExistException;
import com.mehedi.nitex.model.User;
import com.mehedi.nitex.model.UserPrincipal;
import com.mehedi.nitex.repository.UserRepository;
import com.mehedi.nitex.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mehedi.nitex.constant.UserImplConstant.*;
import static com.mehedi.nitex.enumration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
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

    // register user
    @Override
    public User register(String fullName, String username, String email, String password) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
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
    public User findUserByUsername(String username)  {
        return userRepository.findUserByUsername(username);
    }

    // find user by email
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // check and throw an exceptions
    @Override
    public User checkUserExist(String username) throws UserNotFoundException {
        User user = findUserByUsername(username);
        if(user == null) throw new UserNotFoundException("User Not found");
        return user;
    }

    // update user
    @Override
    public User updateUser(String currentUsername, String newFullName, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException{
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFullName(newFullName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        userRepository.save(currentUser);
        return currentUser;
    }

    // encoded password
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // check email or username is valid or not
    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }
}
