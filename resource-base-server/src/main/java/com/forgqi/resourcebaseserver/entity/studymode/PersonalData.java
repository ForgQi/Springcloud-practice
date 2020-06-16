package com.forgqi.resourcebaseserver.entity.studymode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@Entity
public class PersonalData {
    @Id
    private Long id;

    @Column(nullable = false)
    private Double score = 0d;
    @Column(nullable = false)
    private Duration totalTime = Duration.ZERO;
    @Column(nullable = false)
    private Integer totalNumber = 0;
    @Column(nullable = false)
    private Integer succeed = 0;
    @ColumnDefault("0")
    private Integer upVote = 0;

    private Double achievementRatio;
    @JsonIgnore
    @OneToMany(mappedBy = "personalData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //拥有mappedBy注解的实体类为关系被维护端
    private List<StudyMode> studyDataList;

    public PersonalData() {
    }

    public PersonalData(Long id) {
        this.id = id;
    }
}
