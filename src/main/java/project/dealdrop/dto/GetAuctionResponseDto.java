package project.dealdrop.dto;

import lombok.Data;
import project.dealdrop.type.AuctionStatus;

@Data
public class GetAuctionResponseDto {
    private String title;
    private AuctionStatus status;
    private Double startingPrice;
    private Double currentPrice;
    private Integer bidCount;
    private String seller;
    private String highestBidder;
    private String winner;
}
