package project.dealdrop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.dealdrop.entity.Auction;
import project.dealdrop.type.AuctionStatus;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    Optional<Auction> findById(Long id);

    List<Auction> findByStatusAndStartTimeLessThanEqual(AuctionStatus status, LocalDateTime time);

    List<Auction> findByStatusAndEndTimeLessThanEqual(AuctionStatus status, LocalDateTime time);
}
