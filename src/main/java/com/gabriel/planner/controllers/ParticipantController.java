package com.gabriel.planner.controllers;

import com.gabriel.planner.domain.participant.Participant;
import com.gabriel.planner.domain.participant.dto.ParticipantRequestPayloadDTO;
import com.gabriel.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayloadDTO payload) {
        Optional<Participant> participant = this.repository.findById(id);

        if (participant.isPresent()){
            Participant rawParticipant = participant.get();
            rawParticipant.setIsconfirmed(true);
            rawParticipant.setName(payload.name());

            this.repository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }
}
