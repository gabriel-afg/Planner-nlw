package com.gabriel.planner.service;

import com.gabriel.planner.domain.activity.Activity;
import com.gabriel.planner.domain.activity.dto.ActivityData;
import com.gabriel.planner.domain.activity.dto.ActivityRequestPayloadDTO;
import com.gabriel.planner.domain.activity.dto.ActivityResponseDTO;
import com.gabriel.planner.domain.trip.Trip;
import com.gabriel.planner.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponseDTO registerActivity(ActivityRequestPayloadDTO payload, Trip trip) {
        Activity activity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.activityRepository.save(activity);

        return new ActivityResponseDTO(activity.getId());
    }

    public List<ActivityData> getAllActivitiesFromTrip(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
