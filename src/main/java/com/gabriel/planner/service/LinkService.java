package com.gabriel.planner.service;

import com.gabriel.planner.domain.activity.dto.ActivityData;
import com.gabriel.planner.domain.link.Link;
import com.gabriel.planner.domain.link.dto.LinkData;
import com.gabriel.planner.domain.link.dto.LinkRequestPayloadDTO;
import com.gabriel.planner.domain.link.dto.LinkResponseDTO;
import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponseDTO registerLink(LinkRequestPayloadDTO payload, Trip trip) {
        Link link = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(link);

        return new LinkResponseDTO(link.getId());
    }

    public List<LinkData> getAllLinksFromTrip(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkData(link.id(), link.title(), link.url())).toList();
    }
}
