package com.example.DestationApi.DataLoader;

import com.example.DestationApi.model.Destination;
import com.example.DestationApi.Repository.DestinationRepository;



import com.example.DestationApi.model.PlannedRoute;
import com.example.DestationApi.model.RouteDay;

import com.example.DestationApi.Repository.PlannedRouteRepository;



import com.example.DestationApi.model.Hotel; // New Import


import com.example.DestationApi.Repository.HotelRepository; // New Import

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PlannedRouteRepository plannedRouteRepository;

    // --- NEW ---
    @Autowired
    private HotelRepository hotelRepository;
    // --- END NEW ---

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${data.load.destinations}")
    private Resource destinationsResource;

    @Value("${data.load.routes}")
    private Resource routesResource;

    // --- NEW ---
    @Value("${data.load.hotels}")
    private Resource hotelsResource;
    // --- END NEW ---

    // A temporary cache to hold destinations by slug for easy linking
    private Map<String, Destination> destinationCache;

    @Override
    public void run(String... args) throws Exception {

        // --- 1. Load Destinations (MUST BE FIRST) ---
        if (destinationRepository.count() == 0) {
            logger.info("Empty database detected. Loading destinations from JSON...");
            loadDestinations();
        } else {
            logger.info("Database already contains destinations. Skipping destination load.");
        }

        // Build the cache for linking
        buildDestinationCache();

        // --- 2. Load Planned Routes ---
        if (plannedRouteRepository.count() == 0) {
            logger.info("Loading planned routes from JSON...");
            loadPlannedRoutes();
        } else {
            logger.info("Database already contains routes. Skipping route load.");
        }

        // --- 3. NEW: Load Hotels ---
        if (hotelRepository.count() == 0) {
            logger.info("Loading hotels from JSON...");
            loadHotels();
        } else {
            logger.info("Database already contains hotels. Skipping hotel load.");
        }

        logger.info("--- Data loading complete. ---");
    }

    private void buildDestinationCache() {
        // Load all destinations into a Map where the key is the slug
        destinationCache = destinationRepository.findAll().stream()
                .collect(Collectors.toMap(Destination::getSlug, Function.identity()));
        logger.info("Built destination cache with {} items.", destinationCache.size());
    }

    private void loadDestinations() {
        try (InputStream inputStream = destinationsResource.getInputStream()) {
            List<Destination> destinations = objectMapper.readValue(inputStream, new TypeReference<List<Destination>>() {});
            destinationRepository.saveAll(destinations);
            logger.info("Successfully loaded {} destinations.", destinations.size());
        } catch (Exception e) {
            logger.error("Failed to load destinations JSON: {}", e.getMessage(), e);
        }
    }

    private void loadPlannedRoutes() {
        if (destinationCache.isEmpty()) {
            logger.warn("Destinations are not loaded. Cannot link routes. Skipping route loading.");
            return;
        }

        try (InputStream inputStream = routesResource.getInputStream()) {
            // Read as a flexible JsonNode tree to handle linking
            JsonNode rootNode = objectMapper.readTree(inputStream);

            for (JsonNode routeNode : rootNode) {
                // Convert node to the main PlannedRoute object (but 'days' will be empty)
                PlannedRoute route = objectMapper.treeToValue(routeNode, PlannedRoute.class);

                // Manually process the 'days' array to link destinations
                if (routeNode.has("days")) {
                    for (JsonNode dayNode : routeNode.get("days")) {
                        RouteDay day = objectMapper.treeToValue(dayNode, RouteDay.class);

                        // Link the destination
                        if (dayNode.has("destination") && dayNode.get("destination").has("slug")) {
                            String destSlug = dayNode.get("destination").get("slug").asText();
                            Destination linkedDestination = destinationCache.get(destSlug);
                            if (linkedDestination != null) {
                                day.setDestination(linkedDestination);
                            } else {
                                logger.warn("Could not find destination slug '{}' for route '{}'", destSlug, route.getName());
                            }
                        }
                        // Add the processed day to the route
                        route.addDay(day);
                    }
                }
                // Save the complete route with its linked days
                plannedRouteRepository.save(route);
            }
            logger.info("Successfully loaded {} planned routes.", rootNode.size());
        } catch (Exception e) {
            logger.error("Failed to load planned routes JSON: {}", e.getMessage(), e);
        }
    }

    // --- NEW METHOD ---
    private void loadHotels() {
        if (destinationCache.isEmpty()) {
            logger.warn("Destinations are not loaded. Cannot link hotels. Skipping hotel loading.");
            return;
        }

        try (InputStream inputStream = hotelsResource.getInputStream()) {
            // We read as a JsonNode to manually handle the 'destinationSlug'
            JsonNode rootNode = objectMapper.readTree(inputStream);

            for (JsonNode hotelNode : rootNode) {
                // Convert node to the Hotel object
                Hotel hotel = objectMapper.treeToValue(hotelNode, Hotel.class);

                // Link the destination
                if (hotelNode.has("destinationSlug")) {
                    String destSlug = hotelNode.get("destinationSlug").asText();
                    Destination linkedDestination = destinationCache.get(destSlug);
                    if (linkedDestination != null) {
                        hotel.setDestination(linkedDestination);
                        // Save the linked hotel
                        hotelRepository.save(hotel);
                    } else {
                        logger.warn("Could not find destination slug '{}' for hotel '{}'", destSlug, hotel.getName());
                    }
                }
            }
            logger.info("Successfully loaded {} hotels.", rootNode.size());
        } catch (Exception e) {
            logger.error("Failed to load hotels JSON: {}", e.getMessage(), e);
        }
    }
}