package com.abhi.bookmytickets.theatre.repo;

import com.abhi.bookmytickets.theatre.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTheatreIdAndAudit_DeletedAtIsNull(Long theatreId);
}

