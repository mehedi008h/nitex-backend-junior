package com.mehedi.nitex.service;

import com.mehedi.nitex.exceptions.model.*;
import com.mehedi.nitex.model.Book;
import com.mehedi.nitex.model.User;


public interface UserService {
    User register(String fullName, String username, String email, String password) throws UserNotFoundException,EmailExistException, UsernameExistException;
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User checkUserExist(String username) throws UserNotFoundException;

    User updateUser(
            String currentUsername,
            String newFullName,
            String newUsername,
            String newEmail
            ) throws UserNotFoundException, UsernameExistException, EmailExistException;

    User changePassword(
            String username,
            String currentPassword,
            String newPassword
            ) throws UserNotFoundException, PasswordNotMatchException;

    User addCollection(Book book,String username) throws NotFoundException,UserNotFoundException;
    User removeCollection(Book book,String username) throws NotFoundException,UserNotFoundException;
}
