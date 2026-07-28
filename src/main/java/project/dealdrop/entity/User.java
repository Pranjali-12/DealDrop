package project.dealdrop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import project.dealdrop.type.SellerRequest;
import project.dealdrop.type.UserRole;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.BIDDER;

    @Enumerated(EnumType.STRING)
    private SellerRequest sellerRequest=SellerRequest.NONE;

    private String sellerType;

    private String sellerCategory;

    private String sellerReason;

    private Boolean isVerified=false;

    private Boolean isBlocked=false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
