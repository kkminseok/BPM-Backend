package d83t.bpmbackend.domain.aggregate.lounge.entity;

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
    private int likeCount;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityFavorite> likes = new ArrayList<>();

    public void addStoryImage(CommunityImage storyImage) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(storyImage);
    }

    public void updateStoryImage(List<CommunityImage> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addStoryLike(CommunityFavorite like) {
        this.likes.add(like);
        this.likeCount += 1;
    }

    public void removeStoryLike(CommunityFavorite like) {
        this.likes.remove(like);
        this.likeCount -= 1;
    }
}
