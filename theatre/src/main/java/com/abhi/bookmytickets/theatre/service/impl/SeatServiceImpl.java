package com.abhi.bookmytickets.theatre.service.impl;

import com.abhi.bookmytickets.theatre.entity.Seat;
import com.abhi.bookmytickets.theatre.entity.enums.SeatStatus;
import com.abhi.bookmytickets.theatre.exceptions.ResourceNotFoundException;
import com.abhi.bookmytickets.theatre.repo.SeatRepository;
import com.abhi.bookmytickets.theatre.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service("seatServiceImpl")
@Slf4j
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository repo;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final long SEAT_LOCK_TIMEOUT = 30;

    @Override
    public List<Seat> getSeatsByShow(Long showId) {
        String key = "seats:show:" + showId;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.debug("Cache hit for seats of show: {}", showId);
            return (List<Seat>) cached;
        }
        List<Seat> seats = repo.findByShowIdAndAudit_DeletedAtIsNull(showId);
        redisTemplate.opsForValue().set(key, seats, 5, TimeUnit.MINUTES);
        return seats;
    }

    @Override
    public Optional<Seat> getById(Long id) {
        return repo.findById(id).filter(s -> !s.isDeleted());
    }

    @Override
    public CompletableFuture<Boolean> tryLockSeatsAsync(List<Long> seatIds) {
        return CompletableFuture.supplyAsync(() -> {
            for (Long seatId : seatIds) {
                String lockKey = "seat_lock:" + seatId;
                Boolean locked = redisTemplate.opsForValue()
                        .setIfAbsent(lockKey, "LOCKED", SEAT_LOCK_TIMEOUT, TimeUnit.SECONDS);
                if (locked == null || !locked) {
                    unlockSeats(seatIds);
                    return false;
                }
            }
            log.info("Successfully locked seats {}", seatIds);
            return true;
        }, executor);
    }

    @Override
    public CompletableFuture<Void> unlockSeats(List<Long> seatIds) {
        return CompletableFuture.runAsync(() -> {
            seatIds.forEach(seatId -> {
                String lockKey = "seat_lock:" + seatId;
                redisTemplate.delete(lockKey);
            });
            log.info("Unlocked seats {}", seatIds);
        }, executor);
    }

    @Override
    @Transactional
    public void updateSeatStatus(Long seatId, SeatStatus newStatus) {
        Seat seat = repo.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        switch(newStatus) {
            case AVAILABLE -> seat.unlockSeat();
            case LOCKED -> seat.lockSeat();
            case BOOKED -> seat.bookSeat();
            default -> throw new IllegalArgumentException("Invalid SeatStatus: " + newStatus);
        }

        repo.save(seat);
        String cacheKey = "seats_show:" + seat.getShowId();
        redisTemplate.delete(cacheKey);
    }

    @Override
    @Transactional
    public void softDelete(Long seatId) {
        Seat seat = repo.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
        seat.getAudit().markDeleted("User");
        repo.save(seat);
        String cacheKey = "seats:show:" + seat.getShowId();
        redisTemplate.delete(cacheKey);
    }
}
