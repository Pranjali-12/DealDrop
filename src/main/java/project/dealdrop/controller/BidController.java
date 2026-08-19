package project.dealdrop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.BidHistoryResponseDto;
import project.dealdrop.dto.BidResponseDto;
import project.dealdrop.dto.CreateBidRequestDto;
import project.dealdrop.dto.GetMyBidsResponseDto;
import project.dealdrop.entity.User;
import project.dealdrop.security.AuthenticationService;
import project.dealdrop.service.BidService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {
    private final BidService bidService;

    private final AuthenticationService authenticationService;

    @PostMapping("/{auctionId}/placedbid")
    public ResponseEntity<BidResponseDto> placedBid(@RequestBody CreateBidRequestDto createBidRequestDto,@PathVariable Long auctionId){
        User user=authenticationService.getCurrentUser();
        return ResponseEntity.ok(bidService.placedBid(user.getId(),auctionId,createBidRequestDto.getAmount()));
    }

    @GetMapping("/{auctionId}/getBidHistory")
    public ResponseEntity<List<BidHistoryResponseDto>> getBidHistory(@PathVariable Long auctionId){
        return ResponseEntity.ok(bidService.getBidHistory(auctionId));
    }

    @GetMapping("/mybids")
    public ResponseEntity<List<GetMyBidsResponseDto>> getMyBids(){
        User user=authenticationService.getCurrentUser();
        return ResponseEntity.ok(bidService.getMyBids(user.getId()));
    }
}
