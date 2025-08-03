package com.abhi.bookmytickets.theatre.service;

import com.abhi.bookmytickets.theatre.entity.Show;

import java.util.List;
import java.util.Optional;

public interface ShowService {
    Show create(Show show);
    Optional<Show> getById(Long id);
    List<Show> listByTheatre(Long theatreId);
    void softDelete(Long id);
}
