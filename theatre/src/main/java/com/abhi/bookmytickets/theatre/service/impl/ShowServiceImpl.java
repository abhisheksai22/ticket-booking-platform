package com.abhi.bookmytickets.theatre.service.impl;

import com.abhi.bookmytickets.theatre.entity.Show;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.repo.ShowRepository;
import com.abhi.bookmytickets.theatre.service.ShowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("showServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class ShowServiceImpl implements ShowService {

    private final ShowRepository repo;

    @Override
    @Transactional
    public Show create(Show show) {
        return repo.save(show);
    }

    @Override
    public Optional<Show> getById(Long id) {
        return repo.findById(id).filter(s -> !s.isDeleted());
    }

    @Override
    public List<Show> listByTheatre(Long theatreId) {
        return repo.findByTheatreIdAndAudit_DeletedAtIsNull(theatreId);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Show show = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        show.softDelete();
        repo.save(show);
        log.info("Soft deleted show id={}", id);
    }
}
