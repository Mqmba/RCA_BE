package be.api.services.impl;

import be.api.dto.request.CollectionScheduleRequestDTO;
import be.api.dto.request.UserRequestDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.CollectionSchedule;
import be.api.model.User;
import be.api.repository.CollectionScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CollectionScheduleService {
    @Autowired
    private CollectionScheduleRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public CollectionSchedule create(CollectionScheduleRequestDTO scheduleRequestDTO) {
        CollectionSchedule collectionSchedule = modelMapper.map(scheduleRequestDTO, CollectionSchedule.class);
        collectionSchedule.setIsActive(true);
        return repository.save(collectionSchedule);
    }

    public CollectionSchedule getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Page<CollectionSchedule> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public CollectionSchedule update(int id, CollectionScheduleRequestDTO scheduleRequestDTO) {
        CollectionSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        modelMapper.map(scheduleRequestDTO, schedule);
        repository.save(schedule);
        return  schedule;
    }

    public boolean delete(int id) {
        CollectionSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        schedule.setIsActive(false);
        repository.save(schedule);
        return true;
    }
}
