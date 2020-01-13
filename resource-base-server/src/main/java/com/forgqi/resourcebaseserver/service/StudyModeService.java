package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.UnsupportedOperationException;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.studymode.MonthRank;
import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.entity.studymode.StudyMode;
import com.forgqi.resourcebaseserver.entity.studymode.WeekRank;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.repository.studymode.MonthRepository;
import com.forgqi.resourcebaseserver.repository.studymode.PersonalDataRepository;
import com.forgqi.resourcebaseserver.repository.studymode.StudyModeRepository;
import com.forgqi.resourcebaseserver.repository.studymode.WeekRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;
import static java.time.temporal.TemporalAdjusters.next;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyModeService extends AbstractVoteService<WeekRank> {
    public final UserHelper userHelper;
    public final UserRepository userRepository;
    private final StudyModeRepository studyModeRepository;
    private final PersonalDataRepository personalDataRepository;
    private final WeekRepository weekRepository;
    private final MonthRepository monthRepository;

    @Cacheable(cacheNames = "studyMode", key = "target.userHelper.user.username")
    public Optional<StudyMode> creat(Instant estimate) {
        if (Duration.between(Instant.now(), estimate).isNegative()) {
            return Optional.empty();
        }
        return UserHelper.getUserBySecurityContext()
                .map(user -> {
                    try {
                        return studyModeRepository.save(new StudyMode(estimate, new PersonalData(user.getId())));
                    } catch (InvalidDataAccessApiUsageException e) {
                        log.warn(e.getMessage());
                        return studyModeRepository.save(
                                new StudyMode(estimate, personalDataRepository.save(
                                        new PersonalData(user.getId()))));
                    }
                });
    }

    @CacheEvict(cacheNames = "studyMode", key = "target.userHelper.user.username")
    public void delete(Long id) {
        studyModeRepository.deleteById(id);
    }

    @CacheEvict(cacheNames = "studyMode", key = "target.userHelper.user.username")
    public Optional<StudyMode> completed(Long id) {
        return studyModeRepository.findById(id)
                .map(studyMode -> {
                    if (studyMode.getEndTime() == null) {
                        studyMode.setEndTime(Instant.now());
                        studyMode.setTotalTime(Duration.between(studyMode.getCreatedDate(), studyMode.getEndTime()));
                        studyMode.setPoints(studyMode.getTotalTime().minus(Duration.between(studyMode.getEndTime(), studyMode.getSchedule())).toMinutes());
                        studyMode.setFinish(Duration.between(studyMode.getEndTime(), studyMode.getSchedule()).isNegative());
                        PersonalData data = studyMode.getPersonalData();
                        data.setTotalNumber(data.getTotalNumber() + 1);
                        if (studyMode.getFinish()) {
                            data.setSucceed(data.getSucceed() + 1);
                        }
                        data.setScore(studyMode.getPoints() + data.getScore());
                        data.setTotalTime(studyMode.getTotalTime().plus(data.getTotalTime()));
                        data.setAchievementRatio((double) data.getSucceed() / data.getTotalNumber());
                        personalDataRepository.save(data);
                        return studyModeRepository.save(studyMode);
                    }
                    return studyMode;
                });
    }

    public Map<Long, Map<String, ?>> aggregationTotalTime(Instant start, Instant end) {
        return studyModeRepository.findAllByCreatedDateBetween(start, end).parallelStream()
                .collect(Collectors.groupingByConcurrent(studyMode -> studyMode.getPersonalData().getId(), Collectors.summingLong(studyMode -> studyMode.getTotalTime().toMinutes())))
                .entrySet().parallelStream()
                .map(entry -> userRepository.findById(entry.getKey())
                        .map(user -> Pair.of(
                                entry.getKey(),
                                Map.of("sum", entry.getValue(),
                                        "nickName", Optional.ofNullable(user.getNickName()).orElse("兰朵儿"),
                                        "signature", Optional.ofNullable(user.getSignature()).orElse("每天都要好好的，加油！"),
                                        "avatar", Optional.ofNullable(user.getAvatar()))))
                        .get())
                .collect(Collectors.toConcurrentMap(Pair::getFirst, Pair::getSecond));
    }

    public Optional<List<StudyMode>> findPersonalData(Instant start, Instant end) {
        return UserHelper.getUserBySecurityContext()
                .map(user -> studyModeRepository.findAllByCreatedDateBetweenAndPersonalData(start, end, new PersonalData(user.getId())));
    }

    public Optional<PersonalData> findPersonalData() {
        return UserHelper.getUserBySecurityContext()
                .flatMap(user -> personalDataRepository.findById(user.getId()));
    }

    @Override
    @Transactional
    public Optional<WeekRank> vote(Long id, String state) {
        ZonedDateTime zonedDateTime = LocalDate.now().with(next(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault());
        long epochSecond = zonedDateTime.toEpochSecond();
        decide(id + epochSecond, Voted.State.UP);
        Instant now = Instant.now();
        long seconds = Duration.between(now, zonedDateTime).toSeconds();
        Duration duration = Duration.between(now, LocalDate.now().with(firstDayOfNextMonth()).atStartOfDay(ZoneId.systemDefault()));
        monthRepository.findById(id)
                .ifPresentOrElse(monthRank -> {
                    monthRank.setUpVote(monthRank.getUpVote() + 1);
                    monthRepository.save(monthRank);
                }, () -> monthRepository.save(new MonthRank(id, 1, duration.toSeconds())));
        personalDataRepository.findById(id)
                .ifPresent(personalData -> {
                    personalData.setUpVote(personalData.getUpVote() + 1);
                    personalDataRepository.save(personalData);
                });
        return weekRepository.findById(id)
                .map(weekRank -> {
                    weekRank.setUpVote(weekRank.getUpVote() + 1);
                    return weekRepository.save(weekRank);
                }).or(() -> Optional.of(weekRepository.save(new WeekRank(id, 1, seconds))));
    }

    @Override
    protected Voted.Type getType() {
        return Voted.Type.STUDY_MODE;
    }

    @Override
    public CrudRepository<WeekRank, Long> getRepository() {
        throw new UnsupportedOperationException("无返回仓库");
    }
}
