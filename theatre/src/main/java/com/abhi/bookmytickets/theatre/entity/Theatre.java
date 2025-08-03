package com.abhi.bookmytickets.theatre.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="theatres", indexes=@Index(name="idx_theatre_name", columnList="name"))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder=true)
public class Theatre {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Name is mandatory")
    @Column(nullable=false, length=100)
    private String name;

    @Column(length=200)
    private String location;

    @Embedded
    @Builder.Default
    private AuditMetadata audit = AuditMetadata.builder()
            .createdAt(LocalDateTime.now())
            .build();

    @PreUpdate
    public void preUpdate() {
        audit.markUpdated("system");
    }

    public void softDelete() {
        audit.markDeleted("system");
    }

    public boolean isDeleted() {
        return audit.getDeletedAt() != null ;
    }
}


