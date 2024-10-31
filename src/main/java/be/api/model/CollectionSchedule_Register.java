package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CollectionSchedule_Register")
public class CollectionSchedule_Register extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collectionScheduleId")
    private int collection_schedule_id;
    @Column(name = "status")
    private int status;
    @Column(name = "time")
    private int time;
    @Column(name = "materialType")
    private String materialType;
    @ManyToOne
    @JoinColumn(name = "RecycleDepot", referencedColumnName = "RecyclingDepotId")
    private RecyclingDepot recyclingDepot;
    @ManyToOne
    @JoinColumn(name = "CollectionSchedule", referencedColumnName = "CollectionScheduleId")
    private CollectionSchedule collectionSchedule;
    @ManyToOne
    @JoinColumn(name = "Collector", referencedColumnName = "CollectorId")
    private Collector collector;
    @ManyToOne
    @JoinColumn(name = "Resident", referencedColumnName = "ResidentId")
    private Resident resident;
    @ManyToOne
    @JoinColumn(name = "Building", referencedColumnName = "buildingId")
    private Building building;
}
