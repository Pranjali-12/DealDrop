package project.dealdrop.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.BidResponseDto;
import project.dealdrop.entity.Auction;
import project.dealdrop.entity.Bid;
import project.dealdrop.entity.User;
import project.dealdrop.repository.AuctionRepository;
import project.dealdrop.repository.BidRepository;
import project.dealdrop.repository.UserRepository;
import project.dealdrop.type.AuctionStatus;

@Service
@RequiredArgsConstructor
public class BidService {

    private final AuctionRepository auctionRepository;

    private final UserRepository userRepository;

    private final BidRepository bidRepository;

    @Transactional
    public BidResponseDto placedBid(Long userId,Long auctionId,Double amount) {
        Auction auction=auctionRepository.findById(auctionId).orElseThrow();

        User user=userRepository.findById(userId).orElseThrow();

        if(auction.getStatus()!=AuctionStatus.LIVE || auction.getEndTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Auction is not live");
        }

        if(amount<=auction.getCurrentPrice()){
            throw new RuntimeException("Amount should be greater than current price");
        }

        if(user.getId().equals(auction.getSeller().getId())){
            throw new RuntimeException("Seller self won't able to placed the bid");
        }

        Bid bid=new Bid();
        bid.setAmount(amount);
        bid.setAuction(auction);
        bid.setBidder(user);

        System.out.println("bid :"+ bid);

        bidRepository.save(bid);

        auction.setCurrentPrice(amount);
        auction.setBidCount(auction.getBidCount()+1);
        auction.setHighestBidder(user);
        auctionRepository.save(auction);

        System.out.println("auction :"+ auction);

        BidResponseDto response=new BidResponseDto();
        response.setAmount(amount);
        response.setAuctionId(auctionId);
        response.setUserId(userId);
        response.setPlacedAt(bid.getCreatedAt());

        return response;

    }
    
}
