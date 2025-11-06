package com.example.DestationApi.Service;



import com.example.DestationApi.model.PlannedRoute;
import com.example.DestationApi.Repository.PlannedRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This is the "brain" for handling all business logic related to planned routes.
 */
@Service
public class PlannedRouteService {

    @Autowired
    private PlannedRouteRepository plannedRouteRepository;

    /**
     * Gets all planned routes from the database.
     * @return A list of all routes.
     */
    public List<PlannedRoute> getAllRoutes() {
        return plannedRouteRepository.findAll();
    }

    /**
     * Gets a single planned route by its unique ID.
     * @param id The ID of the route.
     * @return An Optional containing the route if found, or empty if not.
     */
    public Optional<PlannedRoute> getRouteById(Long id) {
        return plannedRouteRepository.findById(id);
    }

    /**
     * Gets a single planned route by its URL-friendly slug.
     * This is the main way the front-end will fetch a route.
     * @param slug The slug (e.g., "annapurna-base-camp-trek").
     * @return An Optional containing the route if found, or empty if not.
     */
    public Optional<PlannedRoute> getRouteBySlug(String slug) {
        return plannedRouteRepository.findBySlug(slug);
    }
}
