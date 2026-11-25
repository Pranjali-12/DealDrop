package project.dealdrop.helper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.dealdrop.entity.Auction;
import project.dealdrop.repository.AuctionRepository;
import project.dealdrop.type.AuctionStatus;

@Component
@RequiredArgsConstructor
public class AuctionStatusScheduler {
    private final AuctionRepository auctionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 60000) // every 60 seconds
    @Transactional
    public void updateAuctionStatus(){
        LocalDateTime now=LocalDateTime.now();

        // UPCOMING -> LIVE
        List<Auction> toStart=auctionRepository.findByStatusAndStartTimeLessThanEqual(AuctionStatus.UPCOMING,now);

        for(Auction auction:toStart){
            auction.setStatus(AuctionStatus.LIVE);
            auctionRepository.save(auction);
            messagingTemplate.convertAndSend("/topic/auction"+auction.getId(),"Auction is now LIVE");
        }

        // LIVE -> ENDED
        List<Auction> toEnd=auctionRepository.findByStatusAndendTimeLessThanEqual(AuctionStatus.LIVE,now);

        for(Auction auction:toEnd){
            auction.setStatus(AuctionStatus.ENDED);
            auctionRepository.save(auction);
            messagingTemplate.convertAndSend("/topic/auction"+auction.getId(),
                    "Auction is now Ended. Winner: "+
                    (auction.getWinner()!=null ? auction.getWinner().getName(): "No bids placed"));
        }
    }
}
