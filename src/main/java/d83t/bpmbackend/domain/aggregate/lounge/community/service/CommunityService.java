package d83t.bpmbackend.domain.aggregate.lounge.community.service;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityRequestDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityResponseDto;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {

    CommunityResponseDto createCommunity(CommunityRequestDto communityRequestDto, List<MultipartFile> files, User user);
    List<CommunityResponseDto> getAllCommunity(int page, int size, String sort, User user);
    CommunityResponseDto getCommunity(Long communityId, User user);
    CommunityResponseDto updateCommunity(Long communityId, CommunityRequestDto communityRequestDto, List<MultipartFile> files, User user);
    void deleteCommunity(Long communityId, User user);

    void report(User user, Long communityId, ReportDto reportDto);
}
