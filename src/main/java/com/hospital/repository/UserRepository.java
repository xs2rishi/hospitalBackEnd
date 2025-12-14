package com.hospital.repository;

import com.hospital.entity.User;
import com.hospital.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}
