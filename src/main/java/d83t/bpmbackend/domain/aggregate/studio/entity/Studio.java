package d83t.bpmbackend.domain.aggregate.studio.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "studio")
public class Studio extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String firstTag;

    @Column
    private String secondTag;

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudioKeyword> keywords;

    @Column
    private String phone;

    @Column
    private String sns;

    @Column
    private String openHours;

    @Column
    private String price;

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudioImage> images;

    @Column
    private String content;

    @Column(columnDefinition = "double precision default 0.0")
    private Double rating;

    @Column(columnDefinition = "int default 0")
    private int reviewCount;

    @Column(columnDefinition = "int default 0")
    private int scrapCount;

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @Builder
    public Studio(String name, String address, Double latitude, Double longitude, String firstTag, String secondTag, String phone, String sns, String openHours, String price, List<StudioImage> images, String content, Double rating, int reviewCount, int scrapCount) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstTag = firstTag;
        this.secondTag = secondTag;
        this.phone = phone;
        this.sns = sns;
        this.openHours = openHours;
        this.price = price;
        this.images = images;
        this.content = content;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.scrapCount = scrapCount;
    }

    public void addStudioImage(StudioImage studioImage) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(studioImage);
    }

    public Review addReview(Review review) {
        this.reviews.add(review);
        this.reviewCount += 1;
        return review;
    }


    public void addScrap(Scrap scrap) {
        this.scraps.add(scrap);
        this.scrapCount += 1;
    }

    public void removeScrap(Scrap scrap) {
        this.scraps.remove(scrap);
        this.scrapCount -= 1;
    }

    public void updateRating(Double rating){
        this.rating = rating;
    }
}
