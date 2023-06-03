package d83t.bpmbackend.domain.aggregate.lounge.dto;

import d83t.bpmbackend.domain.aggregate.lounge.entity.Community;
import d83t.bpmbackend.domain.aggregate.lounge.entity.CommunityImage;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "커뮤니티 글 응답 DTO")
public class CommunityResponseDto {

    private Long id;
    private String content;
    private List<String> filesPath;
    private AuthorDto author;
    private int favoriteCount;

    private boolean favorite;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Builder
    public CommunityResponseDto(Community community, boolean favorite) {
        this.id = community.getId();
        this.content = community.getContent();
        this.favoriteCount = community.getFavoriteCount();
        this.favorite = favorite;
        this.createdAt = community.getCreatedDate();
        this.updatedAt = community.getModifiedDate();

        List<String> filePaths = new ArrayList<>();
        for (CommunityImage image : community.getImages()) {
            filePaths.add(image.getStoragePathName());
        }
        this.filesPath = filePaths;

        Profile profile = community.getAuthor();
        this.author = new AuthorDto(profile.getId(), profile.getNickName(), profile.getStoragePathName());
    }

    @Builder
    @Getter
    public static class AuthorDto {
        private Long id;
        private String nickname;
        private String profilePath;
    }

    @Builder
    @Getter
    public static class MultiCommunity {
        List<CommunityResponseDto> communities;
        Integer communityCount;
    }
}
