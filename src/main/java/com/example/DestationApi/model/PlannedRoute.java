package com.example.DestationApi.model;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a full, multi-day planned route or itinerary.
 * This is the "parent" object for a trip.
 * Example: "Annapurna Base Camp Trek"
 */
@Entity
@Table(name = "planned_routes")
public class PlannedRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String duration; // e.g., "10 Days / 9 Nights"

    private String category; // e.g., "Trekking", "Cultural"

    @Column(length = 2048)
    private String description;

    private String imageUrl;

    // This is the core relationship: One Route has Many Days.
    // CascadeType.ALL: If we save a route, save its days. If we delete a route, delete its days.
    // orphanRemoval = true: If we remove a day from this list, delete it from the database.
    @OneToMany(
            mappedBy = "plannedRoute",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER // Eager fetch to get all days when we get a route
    )
    private List<RouteDay> days = new ArrayList<>();

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<RouteDay> getDays() {
        return days;
    }

    public void setDays(List<RouteDay> days) {
        this.days = days;
    }

    // Helper method to add a day
    public void addDay(RouteDay day) {
        days.add(day);
        day.setPlannedRoute(this);
    }
}
