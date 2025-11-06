package com.example.DestationApi.Repository;



import com.example.DestationApi.model.PlannedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlannedRouteRepository extends JpaRepository<PlannedRoute, Long> {

    /**
     * Finds a planned route by its URL-friendly slug.
     */
    Optional<PlannedRoute> findBySlug(String slug);
}
