package com.gabriel.planner.domain.trip.dto;

import java.util.List;

public record TripRequestPayloadDTO(
        String destination,
        String starts_at,
        String ends_at,
        List<String> emails_to_invite,
        String owner_name,
        String owner_email
) {
}
