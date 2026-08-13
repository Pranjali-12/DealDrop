package project.dealdrop.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BidResponseDto {
    private Double amount;
    private Long auctionId;
    private Long userId;
    private LocalDateTime placedAt;
}