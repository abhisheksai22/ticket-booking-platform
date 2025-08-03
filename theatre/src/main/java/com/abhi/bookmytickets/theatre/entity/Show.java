package com.abhi.bookmytickets.theatre.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows", indexes = {
        @Index(name = "idx_show_theatre", columnList = "theatre_id"),
        @Index(name = "idx_show_time", columnList = "start_time")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theatre_id", nullable = false)
    private Long theatreId;

    @Column(nullable = false, length = 100)
    private String movieName;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Embedded
    @Builder.Default
    private AuditMetadata audit = AuditMetadata.builder().createdAt(LocalDateTime.now()).build();

    @PreUpdate
    public void markUpdate() {
        if (audit != null) audit.markUpdated("system");
    }

    public boolean isDeleted() {
        return audit.getDeletedAt() != null;
    }

    public void softDelete() {
        audit.markDeleted("system");
    }
}

