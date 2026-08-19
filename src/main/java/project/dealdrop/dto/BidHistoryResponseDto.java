package project.dealdrop.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BidHistoryResponseDto {
    private String bidderName;
    private Double amount;
    private LocalDateTime placedAt;
}
