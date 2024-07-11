package com.gabriel.planner.domain.activity;

import com.gabriel.planner.domain.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "activities")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "occurs_at",nullable = false)
    private LocalDateTime occursAt;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Activity(String title, String occursAt, Trip trip) {
        this.title = title;
        this.occursAt = LocalDateTime.parse(occursAt);
        this.trip = trip;
    }
}
