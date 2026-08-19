package project.dealdrop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.dealdrop.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid,Long> {

    List<Bid> findAllByAuctionId(Long auctionId);

    List<Bid> findAllByBidderId(Long id);
    
}
