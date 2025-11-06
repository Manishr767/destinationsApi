package com.example.DestationApi.Controller;



import com.example.DestationApi.model.PlannedRoute;
import com.example.DestationApi.Service.PlannedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/routes") // All URLs in this file start with /api/v1/routes
@CrossOrigin(origins = "*") // Allows your front-end to call this API
public class PlannedRouteController {

    @Autowired
    private PlannedRouteService plannedRouteService;

    /**
     * API Endpoint to get all planned routes.
     * URL: GET /api/v1/routes
     */
    @GetMapping
    public List<PlannedRoute> getAllRoutes() {
        return plannedRouteService.getAllRoutes();
    }

    /**
     * API Endpoint to get a single route by its ID.
     * URL: GET /api/v1/routes/1 (or /2, /3, etc.)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlannedRoute> getRouteById(@PathVariable Long id) {
        Optional<PlannedRoute> route = plannedRouteService.getRouteById(id);

        // This is a clean way to return the data or a 404 "Not Found" error
        return route.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API Endpoint to get a single route by its slug.
     * This is the one your website will probably use most!
     * URL: GET /api/v1/routes/slug/annapurna-base-camp-trek
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<PlannedRoute> getRouteBySlug(@PathVariable String slug) {
        Optional<PlannedRoute> route = plannedRouteService.getRouteBySlug(slug);

        return route.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}