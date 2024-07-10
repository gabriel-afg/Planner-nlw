package com.gabriel.planner.domain.trip;

import com.gabriel.planner.domain.trip.dto.TripRequestPayloadDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip(TripRequestPayloadDTO data) {
        this.destination = data.destination();
        this.startAt = LocalDateTime.parse(data.starts_at());
        this.endAt = LocalDateTime.parse(data.ends_at());
        this.ownerName = data.owner_name();
        this.ownerEmail = data.owner_email();
        this.isConfirmed = false;
    }

    public void setConfirmed(boolean b) {
        this.isConfirmed = b;
    }
}
