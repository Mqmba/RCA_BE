package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RecyclingDepot")
public class RecyclingDepot extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecyclingDepotId")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")  // Foreign key to User table
    private User user;

    @Column(name = "DepotName")
    private String depotName;

    @Column(name = "Location")
    private String location;

    @Column(name = "IsWorking")
    private Boolean isWorking; // Assuming this is a boolean indicating if the depot is operational
}
