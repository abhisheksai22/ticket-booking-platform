package com.abhi.bookmytickets.theatre.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditMetadata {
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void markUpdated(String user) {
        this.updatedAt = LocalDateTime.now();
    }

    public void markDeleted(String user) {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
