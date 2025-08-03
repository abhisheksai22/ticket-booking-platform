package com.abhi.bookmytickets.theatre.repo;

import com.abhi.bookmytickets.theatre.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepo extends JpaRepository<Theatre, Long> {
    List<Theatre> findByAudit_DeletedAtIsNull();
}
