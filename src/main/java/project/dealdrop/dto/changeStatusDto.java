package project.dealdrop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.dealdrop.type.SellerRequest;

@Data
public class changeStatusDto {

    @NotNull(message = "Seller request status is required")
    private SellerRequest sellerRequest;
}