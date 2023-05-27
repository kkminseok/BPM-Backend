package d83t.bpmbackend.domain.aggregate.lounge.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Community_Image")
public class CommunityImage extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Community story;

    @Column(name = "filename", nullable = false)
    private String originFileName;

    @Column(name = "path", nullable = false)
    private String storagePathName;
}
