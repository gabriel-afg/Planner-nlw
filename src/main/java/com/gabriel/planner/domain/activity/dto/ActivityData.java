package com.gabriel.planner.domain.activity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(
        UUID id,
        String title,
        LocalDateTime occurs_at
) {
}
