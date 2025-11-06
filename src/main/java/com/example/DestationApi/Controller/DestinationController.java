package com.example.DestationApi.Controller;



import com.example.DestationApi.model.Destination;
import com.example.DestationApi.Service.DestinationService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Keep this

// ADD THIS IMPORT
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

// ADD THIS ANNOTATION
// This tells Spring Boot to allow requests from any origin (*).
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    // GET /api/v1/destinations
    // Gets all destinations
    @GetMapping
    public List<Destination> getAllDestinations() {
        return destinationService.getAllDestinations();
    }

    // GET /api/v1/destinations/{id}
    // Gets a single destination by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        return destinationService.getDestinationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/destinations/slug/{slug}
    // Gets a single destination by its SLUG (e.g., "pokhara")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Destination> getDestinationBySlug(@PathVariable String slug) {
        return destinationService.getDestinationBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/destinations
    // Creates a new destination
    @PostMapping
    public Destination createDestination(@RequestBody Destination destination) {
        return destinationService.createDestination(destination);
    }

    // PUT /api/v1/destinations/{id}
    // Updates an existing destination
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Destination>> updateDestination(@PathVariable Long id, @RequestBody Destination destinationDetails) {
        try {
            return ResponseEntity.ok(destinationService.updateDestination(id, destinationDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/v1/destinations/{id}
    // Deletes a destination
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }
}

