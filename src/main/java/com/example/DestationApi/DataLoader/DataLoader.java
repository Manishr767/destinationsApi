package com.example.DestationApi.DataLoader;

import com.example.DestationApi.model.Destination;
import com.example.DestationApi.Repository.DestinationRepository;



import com.example.DestationApi.model.PlannedRoute;
import com.example.DestationApi.model.RouteDay;

import com.example.DestationApi.Repository.PlannedRouteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PlannedRouteRepository plannedRouteRepository;

    // These values are injected from your application.properties file
    @Value("${data.load.destinations}")
    private Resource destinationsResource;

    @Value("${data.load.routes}")
    private Resource routesResource;

    @Override
    public void run(String... args) throws Exception {
        // Create an ObjectMapper to read JSON
        ObjectMapper mapper = new ObjectMapper();
        // This helps if your JSON has properties your Java object doesn't
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 1. Load Destinations
        if (destinationRepository.count() == 0) {
            try (InputStream inputStream = destinationsResource.getInputStream()) {
                List<Destination> destinations = mapper.readValue(inputStream, new TypeReference<List<Destination>>() {});
                destinationRepository.saveAll(destinations);
                logger.info("Successfully loaded {} destinations from JSON.", destinations.size());
            } catch (Exception e) {
                logger.error("Failed to load destinations JSON: " + e.getMessage());
            }
        } else {
            logger.info("Database already contains destinations. Skipping data load.");
        }

        // 2. Load Planned Routes
        if (plannedRouteRepository.count() == 0) {
            if (destinationRepository.count() == 0) {
                logger.warn("Destinations are not loaded. Cannot link routes. Skipping route loading.");
                return;
            }

            try (InputStream inputStream = routesResource.getInputStream()) {
                // This TypeReference is a list of PlannedRoute objects
                List<PlannedRoute> routes = mapper.readValue(inputStream, new TypeReference<List<PlannedRoute>>() {});

                for (PlannedRoute route : routes) {
                    for (RouteDay day : route.getDays()) {
                        // This is the crucial part: Link the data

                        // Check if the day has a destination slug defined
                        if (day.getDestination() != null && day.getDestination().getSlug() != null) {
                            String slug = day.getDestination().getSlug();

                            // Find the *real* destination from the database
                            destinationRepository.findBySlug(slug).ifPresentOrElse(
                                    // If found, set the full destination object
                                    realDestination -> day.setDestination(realDestination),
                                    // If not found, log a warning and set to null
                                    () -> {
                                        logger.warn("Destination with slug '{}' not found for route '{}'.", slug, route.getName());
                                        day.setDestination(null);
                                    }
                            );
                        }
                        // Important: Set the parent-child relationship
                        day.setPlannedRoute(route);
                    }
                    // Save the fully linked route
                    plannedRouteRepository.save(route);
                }
                logger.info("Successfully loaded {} planned routes from JSON.", routes.size());
            } catch (Exception e) {
                logger.error("Failed to load planned routes JSON: " + e.getMessage());
            }
        } else {
            logger.info("Database already contains routes. Skipping data load.");
        }
    }
}

