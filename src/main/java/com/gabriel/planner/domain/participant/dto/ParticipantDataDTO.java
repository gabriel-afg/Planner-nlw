package com.gabriel.planner.domain.participant.dto;

import java.util.UUID;

public record ParticipantDataDTO(
        UUID id,
        String name,
        String email,
        Boolean isConfirmed
) {
}
