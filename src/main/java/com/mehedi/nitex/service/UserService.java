package com.mehedi.nitex.service;

import com.mehedi.nitex.exceptions.model.EmailExistException;
import com.mehedi.nitex.exceptions.model.UserNotFoundException;
import com.mehedi.nitex.exceptions.model.UsernameExistException;
import com.mehedi.nitex.model.User;

public interface UserService {
    User register(String fullName, String username, String email, String password) throws UserNotFoundException, EmailExistException, UsernameExistException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
