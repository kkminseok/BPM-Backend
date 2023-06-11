package d83t.bpmbackend.domain.aggregate.studio.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import d83t.bpmbackend.domain.aggregate.keyword.entity.Keyword;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studio_keyword")
public class StudioKeyword extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @Column(nullable = false)
    private Integer counting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    public void plusCounting(){
        this.counting += 1;
    }

    public void minusCounting(){
        this.counting -=1;
    }

}
