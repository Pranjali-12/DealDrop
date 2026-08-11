package project.dealdrop.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.CreateAuctionRequestDto;
import project.dealdrop.dto.CreateAuctionResponseDto;
import project.dealdrop.entity.Auction;
import project.dealdrop.entity.User;
import project.dealdrop.repository.AuctionRepository;
import project.dealdrop.security.AuthenticationService;
import project.dealdrop.type.AuctionStatus;
import project.dealdrop.type.UserRole;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    public CreateAuctionResponseDto createAuction(CreateAuctionRequestDto createAuctionRequestDto) {

        if (createAuctionRequestDto.getStartTime().isBefore(LocalDateTime.now())
                || createAuctionRequestDto.getStartTime().isEqual(LocalDateTime.now())) {
            throw new RuntimeException("Enter Valid Start Date");
        }

        if (createAuctionRequestDto.getEndTIme().isBefore(createAuctionRequestDto.getStartTime())
                || createAuctionRequestDto.getEndTIme().isEqual(createAuctionRequestDto.getStartTime())) {
            throw new RuntimeException("Enter Valid End Date");
        }

        User seller = authenticationService.getCurrentUser();

        if (seller.getRole() != UserRole.SELLER) {
            throw new RuntimeException("Only sellers can create auctions.");
        }

        Auction auction = modelMapper.map(createAuctionRequestDto, Auction.class);
        // auction.setTitle(createAuctionRequestDto.getTitle());
        // auction.setDescription(createAuctionRequestDto.getDescription());
        // auction.setStartTime(createAuctionRequestDto.getStartTime());
        // auction.setEndTIme(createAuctionRequestDto.getEndTIme());
        // auction.setStartingPrice(createAuctionRequestDto.getStartingPrice());
        // auction.setImage(createAuctionRequestDto.getImage());
        // auction.setCategory(createAuctionRequestDto.getCategory());
        auction.setCurrentPrice(auction.getStartingPrice());
        auction.setBidCount(0);
        auction.setStatus(AuctionStatus.UPCOMING);
        auction.setCreatedAt(LocalDateTime.now());
        auction.setUpdatedAt(LocalDateTime.now());
        auction.setIsBlocked(false);
        auction.setSeller(seller);

        auctionRepository.save(auction);

        CreateAuctionResponseDto responseDto = modelMapper.map(auction, CreateAuctionResponseDto.class);
        // responseDto.setTitle(auction.getTitle());
        // responseDto.setDescription(auction.getDescription());
        // responseDto.setStartTime(auction.getStartTime());
        // responseDto.setEndTIme(auction.getEndTIme());
        // responseDto.setStartingPrice(auction.getStartingPrice());
        // responseDto.setImage(auction.getImage());
        // responseDto.setStatus(auction.getStatus());
        // responseDto.setCategory(auction.getCategory());
        responseDto.setSellerName(auction.getSeller().getName());

        return responseDto;
    }

}
