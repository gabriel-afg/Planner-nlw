package com.gabriel.planner.controllers;

import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.domain.trip.dto.TripCreateResponseDTO;
import com.gabriel.planner.domain.trip.dto.TripRequestPayloadDTO;
import com.gabriel.planner.repositories.TripRepository;
import com.gabriel.planner.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<TripCreateResponseDTO> createTrip(@RequestBody TripRequestPayloadDTO tripRequestPayloadDTO) {
        Trip newTrip = new Trip(tripRequestPayloadDTO);
        System.out.println(tripRequestPayloadDTO);
        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantToTrip(tripRequestPayloadDTO.emails_to_invite(), newTrip.getId());

        return ResponseEntity.ok().body(new TripCreateResponseDTO(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        return this.tripRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

}
