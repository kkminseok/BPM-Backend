package d83t.bpmbackend.domain.aggregate.studio.service;

import com.querydsl.core.Tuple;
import d83t.bpmbackend.domain.aggregate.keyword.entity.Keyword;
import d83t.bpmbackend.domain.aggregate.keyword.repository.KeywordRepository;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioFilterDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioRequestDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioResponseDto;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.entity.StudioImage;
import d83t.bpmbackend.domain.aggregate.studio.entity.StudioKeyword;
import d83t.bpmbackend.domain.aggregate.studio.repository.ScrapRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioKeywordRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioQueryDSLRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static d83t.bpmbackend.domain.aggregate.keyword.service.KeywordServiceImpl.keywordSymbolMap;
import static d83t.bpmbackend.domain.aggregate.keyword.service.KeywordServiceImpl.reverseKeywordSymbolMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudioServiceImpl implements StudioService {

    private final StudioRepository studioRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final StudioQueryDSLRepository studioQueryDSLRepository;
    private final KeywordRepository keywordRepository;
    private final StudioKeywordRepository studioKeywordRepository;

    @Override
    @Transactional
    public StudioResponseDto createStudio(StudioRequestDto requestDto) {
        Studio studio = requestDto.toEntity();
        //studio.addRecommend(requestDto.getRecommends());

        Studio savedStudio = studioRepository.save(studio);

        return convertDto(savedStudio, false);
    }

    @Override
    public StudioResponseDto findById(Long studioId, User user) {
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));

        boolean isScrapped = checkStudioScrapped(studioId, user);
        return convertDto(studio, isScrapped);
    }

    @Override
    public List<StudioResponseDto> findStudioAll(Integer limit, Integer offset, String condition, String q, User user) {
        int pageSize = limit == null ? 20 : limit;
        int pageNumber = offset == null ? 0 : offset;
        List<Studio> studios = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        if (q != null) {
            studios = studioRepository.searchStudioNames(q);
        } else {
            if (condition == null) {
                studios = studioRepository.findByAll(pageable);
            } else {
                if (condition.equals("review")) {
                    studios = studioRepository.findByAllByReview(pageable);
                } else if (condition.equals("popular")) {
                    studios = studioRepository.findByAllByPopular(pageable);
                } else if (condition.equals("new")) {
                    studios = studioRepository.findByAll(pageable);
                }
            }
        }

        return studios.stream().map(studio -> {
            boolean isScrapped = checkStudioScrapped(studio.getId(), findUser);
            return convertDto(studio, isScrapped);
        }).collect(Collectors.toList());
    }

    @Override
    public List<StudioResponseDto> getFilterStudio(User user, StudioFilterDto studioFilterDto) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));

        List<Long> keyword = studioFilterDto.getKeyword();
        List<Studio> studios = studioQueryDSLRepository.findAllByFilterStudio(keyword);

        return studios.stream().map(studio -> {
            boolean isScrapped = checkStudioScrapped(studio.getId(), findUser);
            return convertDto(studio, isScrapped);
        }).collect(Collectors.toList());
    }

    @Override
    public void plusKeyword(Studio studio, List<Long> keywords) {
        for (Long keyword : keywords) {
            Optional<StudioKeyword> studioKeyword = studioKeywordRepository.findByKeywordIdAndStudioId(keyword, studio.getId());
            //DB에 없는 경우
            if (studioKeyword.isEmpty()) {
                Keyword keyWord = keywordRepository.findById(keyword).get();
                StudioKeyword initStudioKeyword = StudioKeyword.builder()
                        .keyword(keyWord)
                        .studio(studio)
                        .counting(1)
                        .build();
                studioKeywordRepository.save(initStudioKeyword);
            } else {
                //새로 추가해준다.
                StudioKeyword updateStudio = studioKeyword.get();
                updateStudio.plusCounting();
            }
        }
    }

    @Override
    public void minusKeyword(Studio studio, Review review) {
        String keywords = review.getKeywords();
        if (keywords == null) return;
        String[] splitKeyword = keywords.split(",");

        for (String keyword : splitKeyword) {
            Long keywordId = reverseKeywordSymbolMap.get(keyword);
            StudioKeyword findKeyword = studioKeywordRepository.findByKeywordIdAndStudioId(keywordId, studio.getId()).get();
            if (findKeyword.getCounting() == 1) {
                studioKeywordRepository.delete(findKeyword);
            } else {
                findKeyword.minusCounting();
                studioKeywordRepository.save(findKeyword);
            }
        }

    }


    @Override
    public void plusRating(Studio studio, Double rating) {
        if (rating != 0.0) {
            Double avg = ((studio.getRating() * studio.getReviewCount()) + rating) / (studio.getReviewCount() + 1);
            studio.updateRating(avg);
        }
    }

    @Override
    public void minusRating(Studio studio, Double rating) {
        if (rating != 0.0) {
            Double avg = ((studio.getRating() * studio.getReviewCount()) - rating) / (studio.getReviewCount() - 1);
            studio.updateRating(avg);
        }
    }


    private boolean checkStudioScrapped(Long studioId, User user) {
        boolean isScrapped = false;
        if (scrapRepository.existsByStudioIdAndUserId(studioId, user.getId())) {
            isScrapped = true;
        }
        return isScrapped;
    }

    private StudioResponseDto convertDto(Studio studio, boolean isScrapped) {
        List<String> filePaths = new ArrayList<>();
        for (StudioImage image : studio.getImages()) {
            filePaths.add(image.getStoragePathName());
        }

        List<Tuple> topThreeKeywordIds = studioQueryDSLRepository.getTopThreeKeyword(studio.getId());
        topThreeKeywordIds.stream().forEach(System.out::println);
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
