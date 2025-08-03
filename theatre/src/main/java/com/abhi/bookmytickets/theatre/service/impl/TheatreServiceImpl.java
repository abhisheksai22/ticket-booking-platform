package com.abhi.bookmytickets.theatre.service.impl;

import com.abhi.bookmytickets.theatre.entity.Theatre;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.repo.TheatreRepo;
import com.abhi.bookmytickets.theatre.service.TheatreService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("theatreServiceImpl")
@Slf4j
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepo theatreRepo;

    public TheatreServiceImpl(TheatreRepo theatreRepo) {
        this.theatreRepo = theatreRepo;
    }

    @Override
    @Transactional
    public Theatre create(Theatre theatre) {
        return theatreRepo.save(theatre);
    }

    @Override
    public List<Theatre> getAllTheatres(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Theatre> theatrePage = theatreRepo.findAll(pageable);
        return theatrePage.getContent();
    }

    @Override
    public Optional<Theatre> getById(Long id) {
        return theatreRepo.findById(id)
                .filter(t -> !t.isDeleted());
    }

    @Override
    public List<Theatre> listAll() {
        return theatreRepo.findByAudit_DeletedAtIsNull();
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Theatre theatre = theatreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
        theatre.softDelete();
        theatreRepo.save(theatre);
        log.info("Soft deleted theatre id={}", id);
    }

}
