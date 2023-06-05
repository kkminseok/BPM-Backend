package d83t.bpmbackend.domain.aggregate.user.service;

import d83t.bpmbackend.domain.aggregate.lounge.dto.QuestionBoardResponse;
import d83t.bpmbackend.domain.aggregate.lounge.entity.QuestionBoard;
import d83t.bpmbackend.domain.aggregate.lounge.repository.QuestionBoardRepository;
import d83t.bpmbackend.domain.aggregate.lounge.service.QuestionBoardService;
import d83t.bpmbackend.domain.aggregate.lounge.service.QuestionBoardServiceImpl;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileDto;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileRequest;
import d83t.bpmbackend.domain.aggregate.profile.dto.ProfileResponse;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.profile.repository.ProfileRepository;
import d83t.bpmbackend.domain.aggregate.profile.service.ProfileImageService;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioRepository;
import d83t.bpmbackend.domain.aggregate.user.dto.ScheduleRequest;
import d83t.bpmbackend.domain.aggregate.user.dto.ScheduleResponse;
import d83t.bpmbackend.domain.aggregate.user.dto.UserRequestDto;
import d83t.bpmbackend.domain.aggregate.user.entity.Schedule;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.ScheduleRepository;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import d83t.bpmbackend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final ProfileImageService profileImageService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final StudioRepository studioRepository;
    private final ScheduleRepository scheduleRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardServiceImpl questionBoardService;

    private final JwtService jwtService;

    @Override
    public ProfileResponse signUp(ProfileRequest profileRequest, MultipartFile file) {
        Optional<User> findUser = userRepository.findByKakaoId(profileRequest.getKakaoId());
        if (findUser.isPresent()) {
            throw new CustomException(Error.USER_ALREADY_EXITS);
        }
        //닉네임 중복여부
        if (profileRepository.findByNickName(profileRequest.getNickname()).isPresent()) {
            throw new CustomException(Error.USER_NICKNAME_ALREADY_EXITS);
        }
        ProfileDto profileDto = profileImageService.createProfileDto(profileRequest, file);

        Profile profile = profileDto.toEntity();
        String uuid = generateUniqueId();
        User user = User.builder()
                .kakaoId(profileRequest.getKakaoId())
                .profile(profile)
                .uuid(uuid)
                .build();
        userRepository.save(user);
        return ProfileResponse.builder()
                .nickname(profileDto.getNickname())
                .bio(profileDto.getBio())
                .image(profileDto.getImagePath())
                .token(jwtService.createToken(uuid))
                .build();
    }

    @Override
    public ProfileResponse verification(UserRequestDto userRequestDto) {
        User user = userRepository.findByKakaoId(userRequestDto.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_KAKAO_ID));

        Profile profile = user.getProfile();

        return ProfileResponse.builder()
                .nickname(profile.getNickName())
                .bio(profile.getBio())
                .token(jwtService.createToken(user.getUuid()))
                .image(profile.getStoragePathName())
                .build();
    }

    @Override
    public List<QuestionBoardResponse> findAllMyQuestionBoardList(User user, int page, int size) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_KAKAO_ID));

        Pageable pageable = PageRequest.of(page, size);

        Profile profile = findUser.getProfile();

        List<QuestionBoard> questionBoards = questionBoardRepository.findByProfileId(pageable, profile.getId());

        return questionBoards.stream().map(questionBoard -> {
            return questionBoardService.convertResponse(user, questionBoard);
        }).collect(Collectors.toList());
    }




    // TODO: 요건 확실히 정해야함. 스튜디오 없어도 등록되는데, 조회시에는 그 정보를 보여줘야하나?라는 요건
    @Override
    public ScheduleResponse registerSchedule(User user, ScheduleRequest scheduleRequest) {
        Studio studio = studioRepository.findByName(scheduleRequest.getStudioName())
                .orElse(null);
        String studioName = studio == null ? scheduleRequest.getStudioName() : studio.getName();
        Schedule schedule = Schedule.builder()
                .name(scheduleRequest.getScheduleName())
                .studio(studio)
                .user(user)
                .studioName(studioName)
                .date(convertDateFormat(scheduleRequest.getDate()))
                .time(scheduleRequest.getTime())
                .memo(scheduleRequest.getMemo())
                .build();
        scheduleRepository.save(schedule);

        return ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleName(schedule.getName())
                .studioName(schedule.getStudioName())
                .time(schedule.getTime())
                .date(schedule.getDate())
                .memo(schedule.getMemo())
                .build();
    }

    @Override
    public ScheduleResponse updateSchedule(User user, ScheduleRequest scheduleRequest, Long scheduleId) {
        Studio studio = studioRepository.findByName(scheduleRequest.getStudioName())
                .orElse(null);
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_SCHEDULE);
        });

        String studioName = studio == null ? scheduleRequest.getStudioName() : studio.getName();

        findSchedule.setDate(convertDateFormat(scheduleRequest.getDate()));
        findSchedule.setMemo(scheduleRequest.getMemo());
        findSchedule.setName(scheduleRequest.getScheduleName());
        findSchedule.setStudio(studio);
        findSchedule.setTime(scheduleRequest.getTime());
        findSchedule.setStudioName(studioName);

        scheduleRepository.save(findSchedule);

        return ScheduleResponse.builder()
                .id(findSchedule.getId())
                .scheduleName(findSchedule.getName())
                .studioName(findSchedule.getStudioName())
                .time(findSchedule.getTime())
                .date(findSchedule.getDate())
                .memo(findSchedule.getMemo())
                .build();

    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(User user, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_SCHEDULE));
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleName(schedule.getName())
                .date(schedule.getDate())
                .time(schedule.getTime())
                .studioName(schedule.getStudioName())
                .memo(schedule.getMemo())
                .build();
    }

    @Override
    public void deleteSchedule(User user) {
        Schedule schedule = scheduleRepository.findByUserId(user.getId()).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_SCHEDULE));
        scheduleRepository.delete(schedule);
    }


    private LocalDate convertDateFormat(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }


    private String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }
}
