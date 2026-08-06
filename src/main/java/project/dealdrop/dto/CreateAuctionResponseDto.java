package project.dealdrop.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.dealdrop.type.AuctionCategory;
import project.dealdrop.type.AuctionStatus;

@Data
@Getter
@Setter
public class CreateAuctionResponseDto {
    private String title;

    @Column(length = 3000)
    private String description;

    private Double startingPrice;

    private LocalDateTime startTime;

    private LocalDateTime endTIme;

    private String image;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    @Enumerated(EnumType.STRING)
    private AuctionCategory category;

    private String sellerName;

}
