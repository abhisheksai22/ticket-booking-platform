package com.abhi.bookmytickets.theatre.service;

import com.abhi.bookmytickets.theatre.entity.Seat;
import com.abhi.bookmytickets.theatre.entity.enums.SeatStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SeatService {

    List<Seat> getSeatsByShow(Long showId);
    Optional<Seat> getById(Long id);
    CompletableFuture<Boolean> tryLockSeatsAsync(List<Long> seatIds);
    CompletableFuture<Void> unlockSeats(List<Long> seatIds);
    void updateSeatStatus(Long seatId, SeatStatus newStatus);
    void softDelete(Long seatId);

}
