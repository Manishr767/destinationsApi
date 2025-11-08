package com.example.DestationApi.Controller;



import com.example.DestationApi.model.Hotel;
import com.example.DestationApi.Service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@CrossOrigin(origins = "*") // Allows your front-end to call this
public class HotelController {

    @Autowired
    private HotelService hotelService;

    /**
     * API Endpoint to get all hotels.
     * URL: GET /api/v1/hotels
     */
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    /**
     * API Endpoint to get a single hotel by its slug.
     * URL: GET /api/v1/hotels/slug/hotel-fewa
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Hotel> getHotelBySlug(@PathVariable String slug) {
        return hotelService.getHotelBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API Endpoint to get all hotels for a specific destination.
     * This is the one your front-end modal will use!
     * URL: GET /api/v1/hotels/by-destination/phewa-lake
     */
    @GetMapping("/by-destination/{destinationSlug}")
    public ResponseEntity<List<Hotel>> getHotelsByDestination(@PathVariable String destinationSlug) {
        List<Hotel> hotels = hotelService.getHotelsByDestinationSlug(destinationSlug);
        if (hotels.isEmpty()) {
            return ResponseEntity.noContent().build(); // or notFound()
        }
        return ResponseEntity.ok(hotels);
    }
}