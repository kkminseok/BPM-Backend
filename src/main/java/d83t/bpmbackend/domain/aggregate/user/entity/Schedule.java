package d83t.bpmbackend.domain.aggregate.user.entity;

import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime time;

    @Column
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Studio studio;

    @Column
    private String studioName;

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
