package be.api.services;

import be.api.dto.request.CollectionScheduleRequestDTO;
import be.api.dto.request.UserRequestDTO;
import be.api.model.User;

import java.util.List;

public interface ICollectionScheduleServices {
    int saveCollectionSchedule(CollectionScheduleRequestDTO scheduleRequestDTO);
    void updateCollectionSchedule(int id, CollectionScheduleRequestDTO scheduleRequestDTO);
    CollectionScheduleRequestDTO getScheduleById(int id);
    void deleteSchedule(int id);
}
