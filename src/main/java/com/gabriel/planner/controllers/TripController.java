package com.gabriel.planner.controllers;

import com.gabriel.planner.domain.participant.Participant;
import com.gabriel.planner.domain.participant.dto.ParticipantCreateResponseDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantDataDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantRequestPayloadDTO;
import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.domain.trip.dto.TripCreateResponseDTO;
import com.gabriel.planner.domain.trip.dto.TripRequestPayloadDTO;
import com.gabriel.planner.repositories.TripRepository;
import com.gabriel.planner.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(tripRequestPayloadDTO.emails_to_invite(), newTrip);

        return ResponseEntity.ok().body(new TripCreateResponseDTO(newTrip.getId()));
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponseDTO> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayloadDTO payload) {

        return this.tripRepository.findById(id)
                .map(trip -> {
                    ParticipantCreateResponseDTO participantResponse = this.participantService.registerParticipantToTrip(payload.email(), trip);
                    if (trip.getIsConfirmed()) {
                        this.participantService.triggerConfirmationEmailToParticipant(payload.email());
                    }
                    // Correção: Retorna ParticipantCreateResponseDTO com o e-mail do participante
                    return ResponseEntity.ok().body(new ParticipantCreateResponseDTO(participantResponse.id()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        return this.tripRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity confirmTrip(@PathVariable UUID id) {
        return this.tripRepository.findById(id)
                .map(trip -> {
                    trip.setConfirmed(true);
                    this.tripRepository.save(trip);
                    this.participantService.triggerConfirmationEmailToParticipants(trip.getId());
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDataDTO>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantDataDTO> participantList = this.participantService.getAllParticipantsFromTrip(id);

        return ResponseEntity.ok().body(participantList);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayloadDTO tripRequestPayloadDTO) {
        return this.tripRepository.findById(id)
                .map(trip -> {
                    trip.setDestination(tripRequestPayloadDTO.destination());
                    trip.setStartAt(LocalDateTime.parse(tripRequestPayloadDTO.starts_at()));
                    trip.setEndAt(LocalDateTime.parse(tripRequestPayloadDTO.ends_at()));
                    trip.setOwnerName(tripRequestPayloadDTO.owner_name());
                    trip.setOwnerEmail(tripRequestPayloadDTO.owner_email());
                    this.tripRepository.save(trip);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
