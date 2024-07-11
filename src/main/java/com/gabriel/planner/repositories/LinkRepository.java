package com.gabriel.planner.repositories;

import com.gabriel.planner.domain.link.Link;
import com.gabriel.planner.domain.link.dto.LinkData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID>{
    List<LinkData> findByTripId(UUID tripId);
}
