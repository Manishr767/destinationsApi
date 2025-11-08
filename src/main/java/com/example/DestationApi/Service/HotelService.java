package com.example.DestationApi.Service;



import com.example.DestationApi.model.Hotel;
import com.example.DestationApi.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    /**
     * Gets all hotels.
     */
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    /**
     * Gets a single hotel by its slug.
     */
    public Optional<Hotel> getHotelBySlug(String slug) {
        return hotelRepository.findBySlug(slug);
    }

    /**
     * Gets all hotels for a specific destination.
     */
    public List<Hotel> getHotelsByDestinationSlug(String destinationSlug) {
        return hotelRepository.findByDestinationSlug(destinationSlug);
    }
}
