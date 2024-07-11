package com.gabriel.planner.controllers;

import com.gabriel.planner.domain.activity.dto.ActivityData;
import com.gabriel.planner.domain.activity.dto.ActivityRequestPayloadDTO;
import com.gabriel.planner.domain.activity.dto.ActivityResponseDTO;
import com.gabriel.planner.domain.link.dto.LinkData;
import com.gabriel.planner.domain.link.dto.LinkRequestPayloadDTO;
import com.gabriel.planner.domain.link.dto.LinkResponseDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantCreateResponseDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantDataDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantRequestPayloadDTO;
import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.domain.trip.dto.TripCreateResponseDTO;
import com.gabriel.planner.domain.trip.dto.TripRequestPayloadDTO;
import com.gabriel.planner.repositories.TripRepository;
import com.gabriel.planner.service.ActivityService;
import com.gabriel.planner.service.LinkService;
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

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

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

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponseDTO> registerActivities(@PathVariable UUID id, @RequestBody ActivityRequestPayloadDTO payload) {
        return this.tripRepository.findById(id)
                .map(trip -> {
                    ActivityResponseDTO activityResponse = this.activityService.registerActivity(payload, trip);
                    return ResponseEntity.ok().body(activityResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponseDTO> registerLinks(@PathVariable UUID id, @RequestBody LinkRequestPayloadDTO payload) {
        return this.tripRepository.findById(id)
                .map(trip -> {
                    LinkResponseDTO linkResponse = this.linkService.registerLink(payload, trip);
                    return ResponseEntity.ok().body(linkResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllActivities(@PathVariable UUID id) {
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok().body(linkDataList);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllLinks(@PathVariable UUID id) {
        List<ActivityData> activityList = this.activityService.getAllActivitiesFromTrip(id);

        return ResponseEntity.ok().body(activityList);
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
