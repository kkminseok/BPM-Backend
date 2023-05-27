package d83t.bpmbackend.domain.aggregate.lounge.entity;

import d83t.bpmbackend.base.entity.DateEntity;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community_comment")
public class CommunityComment extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Profile author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Community community;

    @Builder.Default
    private int reportCount = 0;


    //글 내용 수정
    public void updateBody(String updateBody) {
        this.body = updateBody;
    }
    // 신고수 추가
    public void plusReport(){
        this.reportCount += 1;
    }
}
