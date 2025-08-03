package com.abhi.bookmytickets.theatre.entity;

import com.abhi.bookmytickets.theatre.entity.enums.SeatStatus;
import com.abhi.bookmytickets.theatre.entity.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seats", indexes = {
        @Index(name = "idx_seat_show", columnList = "show_id"),
        @Index(name = "idx_seat_status", columnList = "status"),
        @Index(name = "idx_seat_theatre", columnList = "theatre_id")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long theatreId;

    private Long showId;

    private String row;

    private Integer number;

    @Enumerated(EnumType.STRING)
    private SeatType type;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Embedded
    @Builder.Default
    private AuditMetadata audit = AuditMetadata.builder().createdAt(java.time.LocalDateTime.now()).build();

    @PreUpdate
    public void markUpdate() {
        if (audit != null) audit.markUpdated("system");
    }

    public boolean isDeleted() {
        return audit.getDeletedAt() != null;

    }

    public void lockSeat() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("Seat is not available to lock.");
        }
        this.status = SeatStatus.LOCKED;
        if (audit != null) audit.markUpdated("system");
    }

    public void bookSeat() {
        if (this.status != SeatStatus.LOCKED) {
            throw new IllegalStateException("Seat must be locked before booking.");
        }
        this.status = SeatStatus.BOOKED;
        if (audit != null) audit.markUpdated("system");
    }

    public void unlockSeat() {
        if (this.status != SeatStatus.LOCKED) {
            throw new IllegalStateException("Only locked seats can be unlocked.");
        }
        this.status = SeatStatus.AVAILABLE;
        if (audit != null) audit.markUpdated("system");
    }

    public void softDelete() {
        if (audit != null) audit.markDeleted("system");
    }

}
