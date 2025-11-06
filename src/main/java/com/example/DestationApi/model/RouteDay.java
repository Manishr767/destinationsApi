package com.example.DestationApi.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Represents a single day within a PlannedRoute.
 * This is the "child" object.
 * Example: "Day 1: Arrival in Pokhara"
 */
@Entity
@Table(name = "route_days")
public class RouteDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayNumber; // e.g., 1, 2, 3

    private String title; // e.g., "Arrival in Kathmandu"

    @Column(length = 1024)
    private String description; // "Arrive at Tribhuvan Airport and transfer to your hotel..."

    // This links the day back to its main route
    // We use @JsonIgnore to avoid an infinite loop when sending JSON
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planned_route_id", nullable = false)
    @JsonIgnore
    private PlannedRoute plannedRoute;

    // This links this day to a specific destination (e.g., Kathmandu)
    // This is what the DataLoader uses to link the JSON data.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id")
    private Destination destination;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlannedRoute getPlannedRoute() {
        return plannedRoute;
    }

    public void setPlannedRoute(PlannedRoute plannedRoute) {
        this.plannedRoute = plannedRoute;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
