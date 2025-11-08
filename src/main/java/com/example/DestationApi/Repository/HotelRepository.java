package com.example.DestationApi.Repository;



import com.example.DestationApi.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    /**
     * Finds a hotel by its unique slug.
     */
    Optional<Hotel> findBySlug(String slug);

    /**
     * Finds all hotels that are linked to a specific destination slug.
     * This is the main query we will use for the front-end.
     */
    List<Hotel> findByDestinationSlug(String destinationSlug);
}
