package d83t.bpmbackend.domain.aggregate.keyword.entity;

import d83t.bpmbackend.domain.aggregate.studio.entity.StudioKeyword;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "keyword")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", nullable = false)
    private String keyword;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudioKeyword> studioKeyword;

}
