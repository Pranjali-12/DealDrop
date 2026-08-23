package project.dealdrop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.dealdrop.entity.User;
import project.dealdrop.type.SellerRequest;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findBysellerRequest(SellerRequest sellerRequest);
}
