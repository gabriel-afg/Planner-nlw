package com.gabriel.planner.service;

import com.gabriel.planner.domain.participant.Participant;
import com.gabriel.planner.domain.participant.dto.ParticipantCreateResponseDTO;
import com.gabriel.planner.domain.participant.dto.ParticipantDataDTO;
import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        this.participantRepository.saveAll(participants);
    }

    public ParticipantCreateResponseDTO registerParticipantToTrip(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);

        return new ParticipantCreateResponseDTO(participant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public List<ParticipantDataDTO> getAllParticipantsFromTrip(UUID id) {
        return this.participantRepository.findByTripId(id).stream().map(participant -> new ParticipantDataDTO(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsconfirmed())).toList();
    }
}
