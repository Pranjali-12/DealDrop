package project.dealdrop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.dealdrop.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);
}
