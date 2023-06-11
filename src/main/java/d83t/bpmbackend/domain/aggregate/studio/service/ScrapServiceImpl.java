package d83t.bpmbackend.domain.aggregate.studio.service;

import com.querydsl.core.Tuple;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioResponseDto;
import d83t.bpmbackend.domain.aggregate.studio.entity.Scrap;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.entity.StudioImage;
import d83t.bpmbackend.domain.aggregate.studio.repository.ScrapRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioQueryDSLRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static d83t.bpmbackend.domain.aggregate.keyword.service.KeywordServiceImpl.keywordSymbolMap;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;
    private final StudioQueryDSLRepository studioQueryDSLRepository;


    @Override
    public void createScrap(Long studioId, User user) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));

        Scrap scrap = Scrap.builder()
                .studio(studio)
                .user(findUser)
                .build();

        studio.addScrap(scrap);
        studioRepository.save(studio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudioResponseDto> findAllScrappedStudio(User user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Page<Scrap> scraps = scrapRepository.findByUserId(findUser.getId(), pageable);

        return scraps.stream().map(scrap -> convertDto(scrap.getStudio(), true)).collect(Collectors.toList());
    }

    @Override
    public void deleteScrap(Long studioId, User user) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));
        Scrap scrap = scrapRepository.findByStudioIdAndUserId(studioId, user.getId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SCRAP));

        if (scrap.getUser().getId().equals(findUser.getId())) {
            studio.removeScrap(scrap);
            studioRepository.save(studio);
        } else {
            throw new CustomException(Error.NOT_MATCH_USER);
        }
    }

    private StudioResponseDto convertDto(Studio studio, boolean isScrapped) {
        List<String> filePaths = new ArrayList<>();
        for (StudioImage image : studio.getImages()) {
            filePaths.add(image.getStoragePathName());
        }

        List<Tuple> topThreeKeywordIds = studioQueryDSLRepository.getTopThreeKeyword(studio.getId());

        Map<String, Integer> keywords = new LinkedHashMap<>();
        topThreeKeywordIds.stream()
                .forEach(tuple -> {
                    String keyword = keywordSymbolMap.get(tuple.get(0, Integer.class));
                    Integer count = tuple.get(1, Integer.class);
                    keywords.put(keyword, count);
                });


        return StudioResponseDto.builder()
                .id(studio.getId())
                .name(studio.getName())
                .address(studio.getAddress())
                .latitude(studio.getLatitude())
                .longitude(studio.getLongitude())
                .firstTag(studio.getFirstTag())
                .secondTag(studio.getSecondTag())
                .topRecommends(keywords)
                .phone(studio.getPhone())
                .sns(studio.getSns())
                .openHours(studio.getOpenHours())
                .price(studio.getPrice())
                .filesPath(filePaths)
                .content(studio.getContent())
                .rating(studio.getRating())
                .reviewCount(studio.getReviewCount())
                .scrapCount(studio.getScrapCount())
                .isScrapped(isScrapped)
                .createdAt(studio.getCreatedDate())
                .updatedAt(studio.getModifiedDate())
                .build();
    }
}
