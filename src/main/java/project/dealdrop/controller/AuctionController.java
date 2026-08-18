package project.dealdrop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.CreateAuctionRequestDto;
import project.dealdrop.dto.CreateAuctionResponseDto;
import project.dealdrop.entity.Auction;
import project.dealdrop.entity.User;
import project.dealdrop.security.AuthenticationService;
import project.dealdrop.service.AuctionService;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/create")
    public ResponseEntity<CreateAuctionResponseDto> createAuction(@RequestBody CreateAuctionRequestDto createAuctionRequestDto){
        return ResponseEntity.ok(auctionService.createAuction(createAuctionRequestDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<Auction>> GetAllAuctions(){
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<Auction> GetAuctionById(@PathVariable Long auctionId){
        return ResponseEntity.ok(auctionService.getAuctionById(auctionId));
    }

    @GetMapping("/myauctions")
    public ResponseEntity<List<Auction>> GetMyAuctions(){
        User user=authenticationService.getCurrentUser();
        return ResponseEntity.ok(auctionService.getMyAuctions(user));
    }
}
