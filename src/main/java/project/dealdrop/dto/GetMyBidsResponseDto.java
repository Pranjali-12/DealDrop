package project.dealdrop.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GetMyBidsResponseDto {
    private String auctionTitle;
    private Double amount;
    private LocalDateTime placedAt;
}
