package com.geekster.project.assignment.UniversityEventManagement.Service;

import com.geekster.project.assignment.UniversityEventManagement.Model.User;
import com.geekster.project.assignment.UniversityEventManagement.Repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(User user) {
        if (userRepo.existsByUsername(user.getUsername()) || userRepo.existsByEmail(user.getEmail())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public boolean updateProfile(Long userId, String firstName, String lastName, String email, String password) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return false;
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        return true;
    }
} 