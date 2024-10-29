package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Collector")
public class Collector extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CollectorId")
    private int collectorId;

    @Column(name = "VehicleType")
    private Integer vehicleType;

    @Column(name = "VehicleLicensePlate")
    private String vehicleLicensePlate;

    @Column(name = "Rate")
    private Integer rate;

    @Column(name = "NumberPoint")
    private Integer numberPoint;

    @Column(name = "IsWorking")
    private Boolean isWorking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")  // Foreign key to User table
    private User user;
}
