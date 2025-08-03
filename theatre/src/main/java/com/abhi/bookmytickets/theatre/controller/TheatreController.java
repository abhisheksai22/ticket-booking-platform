package com.abhi.bookmytickets.theatre.controller;

import com.abhi.bookmytickets.theatre.entity.Theatre;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Theatre theatre) {
        Theatre created = theatreService.create(theatre);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> get(@PathVariable Long id) {
        return theatreService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
    }

    @GetMapping
    public List<Theatre> list() {
        return theatreService.listAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        theatreService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
