package com.abhi.bookmytickets.theatre.controller;

import com.abhi.bookmytickets.theatre.entity.Show;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Show show, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getAllErrors());
        }
        Show created = showService.create(show);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Show> get(@PathVariable Long id) {
        return showService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
    }

    @GetMapping("/theatre/{theatreId}")
    public List<Show> listByTheatre(@PathVariable Long theatreId) {
        return showService.listByTheatre(theatreId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

