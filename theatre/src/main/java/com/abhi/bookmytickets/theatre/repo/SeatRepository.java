package com.abhi.bookmytickets.theatre.repo;

import com.abhi.bookmytickets.theatre.entity.Seat;
import com.abhi.bookmytickets.theatre.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowIdAndAudit_DeletedAtIsNull(Long showId);

    List<Seat> findByShowIdAndStatusAndAudit_DeletedAtIsNull(Long showId, SeatStatus status);
}
