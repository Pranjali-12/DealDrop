package project.dealdrop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.CreateAuctionRequestDto;
import project.dealdrop.dto.CreateAuctionResponseDto;
import project.dealdrop.dto.GetAuctionResponseDto;
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

    private GetAuctionResponseDto mapToDto(Auction auction) {
        GetAuctionResponseDto dto=new GetAuctionResponseDto();
        dto.setTitle(auction.getTitle());
        dto.setStatus(auction.getStatus());
        dto.setStartingPrice(auction.getStartingPrice());
        dto.setCurrentPrice(auction.getCurrentPrice());
        dto.setBidCount(auction.getBidCount());
        
        dto.setSeller(
                auction.getSeller() != null ? auction.getSeller().getName() : null);

        dto.setHighestBidder(
                auction.getHighestBidder() != null ? auction.getHighestBidder().getName() : null);

        dto.setWinner(
                auction.getWinner() != null ? auction.getWinner().getName() : null);

        return dto;
    }

    public List<GetAuctionResponseDto> getAllAuctions() {

        List<Auction> list = auctionRepository.findAll();
        List<GetAuctionResponseDto> dtoList = list.stream()
                .map(this::mapToDto)
                .toList();
        return dtoList;

    }

    public GetAuctionResponseDto getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow();
        GetAuctionResponseDto auctionDto = mapToDto(auction);
        return auctionDto;
    }

    public List<GetAuctionResponseDto> getMyAuctions(User user) {
        List<Auction> list = auctionRepository.findAllBySellerId(user.getId());
        List<GetAuctionResponseDto> dtoList = list.stream()
                .map(this::mapToDto)
                .toList();
        return dtoList;
    }

}
