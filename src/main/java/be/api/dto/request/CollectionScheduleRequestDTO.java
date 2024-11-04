package be.api.dto.request;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionScheduleRequestDTO {
    private Date scheduleDate;
}
