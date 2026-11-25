package project.dealdrop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestSellerDto {

    @NotBlank(message = "Seller category is required")
    @Size(max = 50, message = "Seller category must be at most 50 characters")
    private String sellerCategory;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String sellerReason;
}
