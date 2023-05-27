package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.domain.aggregate.user.entity.User;

public interface CommunityFavoriteService {
    void createCommunityFavorite(Long storyId, User user);
    void deleteCommunityFavorite(Long storyId, User user);
}
