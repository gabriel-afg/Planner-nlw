package com.gabriel.planner.domain.link.dto;

import java.util.UUID;

public record LinkData(
        UUID id,
        String title,
        String url
) {
}
