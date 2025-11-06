package com.example.DestationApi.Repository;

import com.example.DestationApi.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository tells Spring this is a repository bean
// By extending JpaRepository, we get all standard CRUD methods for free
@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    /**
     * Finds a destination by its URL-friendly slug.
     * This is the method your website will use to get destination details.
     * Spring Data JPA automatically creates the query from the method name.
     */
    Optional<Destination> findBySlug(String slug);
}
