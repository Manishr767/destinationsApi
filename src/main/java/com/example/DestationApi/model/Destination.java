package com.example.DestationApi.model;





import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String province;
    private String category;

    @Column(length = 255)
    private String shortDescription;

    @Column(length = 2048)
    private String longDescription;

    // --- THIS IS THE UPDATED FIELD ---
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "destination_images", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;
    // --- END OF UPDATE ---

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "destination_attractions", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "attraction")
    private List<String> keyAttractions;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    // --- UPDATED GETTER/SETTER ---
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    // --- END OF UPDATE ---

    public List<String> getKeyAttractions() { return keyAttractions; }
    public void setKeyAttractions(List<String> keyAttractions) { this.keyAttractions = keyAttractions; }
}