package com.example.DestationApi.Service;

import com.example.DestationApi.model.Destination;
import com.example.DestationApi.Repository.DestinationRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// @Service marks this class as the business logic layer
@Service
public class DestinationService {

    // @Autowired injects the DestinationRepository bean
    @Autowired
    private DestinationRepository destinationRepository;

    // GET all
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    // GET by ID
    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    // GET by Slug (for your website)
    public Optional<Destination> getDestinationBySlug(String slug) {
        return destinationRepository.findBySlug(slug);
    }

    // POST (Create)
    public Destination createDestination(Destination destination) {
        // Here you might add logic to auto-generate a slug if it's not provided
        if (destination.getSlug() == null || destination.getSlug().isEmpty()) {
            destination.setSlug(destination.getName().toLowerCase().replaceAll("\\s+", "-"));
        }
        return destinationRepository.save(destination);
    }

    // PUT (Update)
    public Optional<Destination> updateDestination(Long id, Destination destinationDetails) {
        // Check if the destination exists
        return destinationRepository.findById(id).map(existingDestination -> {
            // Update the fields
            existingDestination.setName(destinationDetails.getName());
            existingDestination.setSlug(destinationDetails.getSlug());
            existingDestination.setProvince(destinationDetails.getProvince());
            existingDestination.setCategory(destinationDetails.getCategory());
            existingDestination.setShortDescription(destinationDetails.getShortDescription());
            existingDestination.setLongDescription(destinationDetails.getLongDescription());

            // --- THIS IS THE UPDATED LINE ---
            existingDestination.setImageUrls(destinationDetails.getImageUrls());
            // --- END OF UPDATE ---

            existingDestination.setKeyAttractions(destinationDetails.getKeyAttractions());

            // Save the updated entity
            return destinationRepository.save(existingDestination);
        });
    }

    // DELETE
    public boolean deleteDestination(Long id) {
        // Check if the destination exists
        return destinationRepository.findById(id).map(destination -> {
            // If it exists, delete it
            destinationRepository.delete(destination);
            return true;
        }).orElse(false); // Otherwise, return false
    }
}

