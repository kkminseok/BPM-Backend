package d83t.bpmbackend.domain.aggregate.user.repository;

import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.Schedule;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    StudioRepository studioRepository;


    @Autowired
    UserRepository userRepository;

    private Studio studio;
    private User user;

    @BeforeEach
    void setup() {

        studio = Studio.builder()
                .name("스튜디오")
                .firstTag("123")
                .secondTag("second")
                .build();
        studioRepository.save(studio);

        user = User.builder()
                .profile(Profile.builder()
                        .bio("bio")
                        .nickName("nickname")
                        .originFileName("")
                        .storagePathName("")
                        .build())
                .kakaoId(1234L)
                .build();
        userRepository.save(user);
    }

    /*
    @Test
    void 스케줄조회하기() {
        Schedule schedule = Schedule.builder()
                .name("스케줄 이름")
                .user(user)
                .studio(studio)
                .memo("메모")
                .date(convertDateFormat("2022-12-12"))
                .time(convertTimeFormat("17:54:22"))
                .build();
        scheduleRepository.save(schedule);

        Optional<Schedule> findUser = scheduleRepository.findByUserId(user.getId());
        Assertions.assertThat(findUser.get().getMemo()).isEqualTo(schedule.getMemo());
        Assertions.assertThat(findUser.get().getDate()).isEqualTo(schedule.getDate());
        Assertions.assertThat(findUser.get().getTime()).isEqualTo(schedule.getTime());
        Assertions.assertThat(findUser.get().getUser()).isEqualTo(schedule.getUser());
        Assertions.assertThat(findUser.get().getStudio()).isEqualTo(schedule.getStudio());

    }

     */

    @Test
    void 스케줄등록하기() {

        Schedule schedule = Schedule.builder()
                .name("스케줄이름")
                .user(user)
                .studio(studio)
                .memo("메모")
                .date(convertDateFormat("2022-12-12"))
                //.time(convertTimeFormat("17:54:22"))
                .build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        Assertions.assertThat(savedSchedule.getDate()).isEqualTo("2022-12-12");
        Assertions.assertThat(savedSchedule.getTime()).isEqualTo("17:54:22");
        Assertions.assertThat(savedSchedule.getMemo()).isEqualTo("메모");

    }

    @Test
    void 스케줄등록하기_스튜디오없을때(){
        Studio studio = null;
        Schedule schedule = Schedule.builder()
                .name("스케줄 이름")
                .user(user)
                .studio(studio)
                .memo("메모")
                .date(convertDateFormat("2022-12-12"))
                //.time(convertTimeFormat("17:54:22"))
                .build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        Assertions.assertThat(savedSchedule.getDate()).isEqualTo("2022-12-12");
        Assertions.assertThat(savedSchedule.getTime()).isEqualTo("17:54:22");
        Assertions.assertThat(savedSchedule.getMemo()).isEqualTo("메모");
    }

    private LocalDate convertDateFormat(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }

    private LocalTime convertTimeFormat(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(time, dateTimeFormatter);
    }
}