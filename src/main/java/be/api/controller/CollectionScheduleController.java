package be.api.controller;

import be.api.dto.request.CollectionScheduleRequestDTO;
import be.api.dto.request.UserRequestDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.exception.ResourceNotFoundException;
import be.api.model.CollectionSchedule;
import be.api.services.impl.CollectionScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collectionSchedules")
public class CollectionScheduleController {

    @Autowired
    private CollectionScheduleService collectionScheduleService;

    @PostMapping("add")
    public ResponseData<?> addCollectionSchedule(@Valid @RequestBody CollectionScheduleRequestDTO scheduleRequestDTO) {
        try{
            CollectionSchedule collectionSchedule = collectionScheduleService.create(scheduleRequestDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Collection Schedule added successfully", collectionSchedule);
        }
        catch (ResourceNotFoundException e){
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("get-schedule-by-id/{id}")
    public ResponseData<?> getScheduleById(@PathVariable int id) {
            CollectionSchedule schedule = collectionScheduleService.getById(id);
            if (schedule != null)
                return new ResponseData<>(HttpStatus.OK.value(), "Schedule found", schedule);
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Schedule not found");
    }

    @GetMapping("get-list-schedule-by-paging")
    public ResponseData<?> getAllSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CollectionSchedule> schedules = collectionScheduleService.getAll(page, size);
        if (schedules.isEmpty())
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "List schedule not found");
        return new ResponseData<>(HttpStatus.OK.value(), "List schedule found", schedules);
    }


    @PostMapping("update-schedule/{id}")
    public ResponseData<?> updateSchedule(@PathVariable int id, @RequestBody CollectionScheduleRequestDTO scheduleRequestDTO) {
        CollectionSchedule collectionSchedule = collectionScheduleService.update(id, scheduleRequestDTO);
        if(collectionSchedule != null)
            return new ResponseData<>(HttpStatus.OK.value(), "Schedule updated successfully", collectionSchedule);
        return new ResponseError(HttpStatus.NOT_FOUND.value(), "Schedule not found");
    }

    @DeleteMapping("delete-schedule-by-id/{id}")
    public ResponseData<?> deleteSchedule(@PathVariable int id) {
        boolean isDeleted = collectionScheduleService.delete(id);
        if (isDeleted)
            return new ResponseData<>(HttpStatus.OK.value(), "Schedule deleted successfully", true);
        return new ResponseError(HttpStatus.NOT_FOUND.value(), "Schedule not found");
    }
}
