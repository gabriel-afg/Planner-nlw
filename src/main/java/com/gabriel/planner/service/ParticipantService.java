package com.gabriel.planner.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    public void registerParticipantToTrip(List<String> emails, UUID id) {
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}
}
