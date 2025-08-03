package com.abhi.bookmytickets.theatre.controller;

import com.abhi.bookmytickets.theatre.entity.Seat;
import com.abhi.bookmytickets.theatre.entity.enums.SeatStatus;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/show/{showId}")
    public List<Seat> getSeatsByShow(@PathVariable Long showId) {
        return seatService.getSeatsByShow(showId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> get(@PathVariable Long id) {
        return seatService.getById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockSeats(@RequestBody List<Long> seatIds) throws ExecutionException, InterruptedException {
        boolean locked = seatService.tryLockSeatsAsync(seatIds).get();
        if (!locked) {
            return ResponseEntity.status(409).body("Some seats could not be locked (possibly already locked/booked)");
        }
        return ResponseEntity.ok("Seats locked");
    }

    @PostMapping("/unlock")
    public ResponseEntity<?> unlockSeats(@RequestBody List<Long> seatIds) throws ExecutionException, InterruptedException {
        seatService.unlockSeats(seatIds).get();
        return ResponseEntity.ok("Seats unlocked");
    }

    @PutMapping("/{seatId}/status/{status}")
    public ResponseEntity<?> updateSeatStatus(@PathVariable Long seatId, @PathVariable SeatStatus status) {
        seatService.updateSeatStatus(seatId, status);
        return ResponseEntity.ok("Seat status updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

