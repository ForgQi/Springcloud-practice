package com.forgqi.resourcebaseserver.entity.studymode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "createdDate")})
public class StudyMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate = Instant.now();

    @Column(updatable = false)
    private Instant schedule;

    private Instant endTime;

    @Column(nullable = false)
    private Long points = 0L;

    @Column(nullable = false)
    private Duration totalTime = Duration.ZERO;

    private Boolean finish = false;
    @JsonIgnore
    @JoinColumn//设置在PersonalStudyData表中的关联字段(外键)
    // optional属性是定义该关联类是否必须存在，(拥有mappedBy的是被维护端)
    // 值为false 时，关联类双方都必须存在，如果关系被维护端不存在，查询的结果为null。
    // 值为true 时, 关系被维护端可以不存在，查询的结果仍然会返回关系维护端，
    // 在关系维护端中指向关系被维护端的属性为null。optional属性的默认值是true。
    // optional 属性实际上指定关联类与被关联类的join 查询关系，
    // 如optional=false 时join 查询关系为inner join, optional=true 时join 查询关系为left join。
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    private PersonalData personalData;

    public StudyMode() {
    }

    public StudyMode(Instant schedule, PersonalData personalData) {
        this.schedule = schedule;
        this.personalData = personalData;
    }
}
