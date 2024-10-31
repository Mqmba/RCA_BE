package be.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CollectionSchedule")
public class CollectionSchedule extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CollectionScheduleId")
    private int collectionScheduleId;
    @Column(name = "scheduleDate")
    private Date scheduleDate;

}
