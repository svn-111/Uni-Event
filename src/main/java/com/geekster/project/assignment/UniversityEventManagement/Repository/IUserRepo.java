package com.geekster.project.assignment.UniversityEventManagement.Repository;

import com.geekster.project.assignment.UniversityEventManagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 