package com.abhi.bookmytickets.theatre.service;

import com.abhi.bookmytickets.theatre.entity.Theatre;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Optional;

public interface TheatreService {
    Theatre create(Theatre theatre);
    List<Theatre> getAllTheatres(Integer start, Integer size);
    Optional<Theatre> getById(Long id);
    List<Theatre> listAll();
    void softDelete(Long id);
}
