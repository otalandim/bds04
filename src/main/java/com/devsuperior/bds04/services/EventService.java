package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    static final String EVENT_NOT_FOUND = "Event not found";

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        Page<Event> events = repository.findAll(pageable);
        return events.map(e -> new EventDTO(e));
    }

    @Transactional
    public EventDTO create(EventDTO eventDto) {
        try {
            Event event = new Event();
            event.setName(eventDto.getName());
            event.setDate(eventDto.getDate());
            event.setUrl(eventDto.getUrl());
            event.setCity(new City(eventDto.getCityId(), eventDto.getName()));
            event = repository.save(event);
            return new EventDTO(event);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(EVENT_NOT_FOUND);
        }
    }
}
