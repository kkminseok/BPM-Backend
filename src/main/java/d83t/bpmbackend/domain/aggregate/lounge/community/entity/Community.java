package d83t.bpmbackend.domain.aggregate.lounge.community.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Community")
public class Community extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityImage> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Profile author;

    @Column(columnDefinition = "int default 0")
    private int favoriteCount;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityFavorite> favorites = new ArrayList<>();

    @Builder.Default
    private int reportCount = 0;

    public void addCommunityImage(CommunityImage storyImage) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(storyImage);
    }

    public void updateCommunityImage(List<CommunityImage> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addCommunityFavorite(CommunityFavorite like) {
        this.favorites.add(like);
        this.favoriteCount += 1;
    }

    public void removeCommunityFavorite(CommunityFavorite like) {
        this.favorites.remove(like);
        this.favoriteCount -= 1;
    }

    // 신고수 추가
    public void plusReport(){
        this.reportCount += 1;
    }
}
