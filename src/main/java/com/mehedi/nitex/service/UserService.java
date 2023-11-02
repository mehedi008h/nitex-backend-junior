package com.mehedi.nitex.service;

import com.mehedi.nitex.model.User;

public interface UserService {
    User register(String fullName, String username, String email, String password) ;

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
