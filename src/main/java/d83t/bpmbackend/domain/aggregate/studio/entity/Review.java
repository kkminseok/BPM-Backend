package d83t.bpmbackend.domain.aggregate.studio.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Profile author;

    @Column
    private Double rating;

    private int reportCount;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images;

    @Column
    private String content;

    @Column(columnDefinition = "int default 0")
    private int favoriteCount;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> favorite = new ArrayList<>();

    @Column
    private String keywords;


    public void addReviewImage(ReviewImage image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }

    public void updateReviewImage(List<ReviewImage> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addLike(Like like, Profile user) {
        this.favorite.add(like);
        this.favoriteCount += 1;
    }

    public void removeLike(Like like) {
        this.favorite.remove(like);
        this.favoriteCount -= 1;
    }

    // 신고수 추가
    public void plusReport(){
        this.reportCount += 1;
    }
}
