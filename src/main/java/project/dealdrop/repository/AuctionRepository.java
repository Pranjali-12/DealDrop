package project.dealdrop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.dealdrop.entity.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    
}
