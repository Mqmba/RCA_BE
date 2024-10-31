package be.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Building")
public class Building extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BuildingId")
    private int buildingId;
    @Column(name = "name")
    private String buildingName;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;
}
