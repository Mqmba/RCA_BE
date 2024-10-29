package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Resident")
public class Resident extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ResidentId")  // Matches database schema
    private int residentId;

    @Column(name = "RewardPoints")
    private int rewardPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")  // Foreign key to User table
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApartmentId", referencedColumnName = "ApartmentId")  // Foreign key to Apartment table
    private Apartment apartment;
}
