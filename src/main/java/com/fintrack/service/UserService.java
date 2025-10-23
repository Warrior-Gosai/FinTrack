package com.fintrack.service;

import com.fintrack.model.User;

public interface UserService {
    void saveUser(User user);

    boolean isUserAlreadyRegistered(String email);

    User findByEmail(String email);

}
