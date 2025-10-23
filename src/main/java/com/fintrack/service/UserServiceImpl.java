package com.fintrack.service;

import com.fintrack.model.User;
import com.fintrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isUserAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
