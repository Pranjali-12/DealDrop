package project.dealdrop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.dealdrop.dto.CreateAuctionRequestDto;
import project.dealdrop.dto.CreateAuctionResponseDto;
import project.dealdrop.service.AuctionService;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/create")
    public CreateAuctionResponseDto createAuction(@RequestBody CreateAuctionRequestDto createAuctionRequestDto){
        return auctionService.createAuction(createAuctionRequestDto);
    }
}
