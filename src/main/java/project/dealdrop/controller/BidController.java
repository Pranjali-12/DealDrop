package project.dealdrop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.BidResponseDto;
import project.dealdrop.dto.CreateBidRequestDto;
import project.dealdrop.entity.User;
import project.dealdrop.security.AuthenticationService;
import project.dealdrop.service.BidService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class BidController {
    private final BidService bidService;

    private final AuthenticationService authenticationService;

    @PostMapping("/{auctionId}/placedbid")
    public ResponseEntity<BidResponseDto> placedBid(@RequestBody CreateBidRequestDto createBidRequestDto,@PathVariable Long auctionId){
        User user=authenticationService.getCurrentUser();
        return ResponseEntity.ok(bidService.placedBid(user.getId(),auctionId,createBidRequestDto.getAmount()));
    }
}
