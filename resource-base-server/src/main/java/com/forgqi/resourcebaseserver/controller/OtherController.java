package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.entity.GradeOnly;
import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.entity.studymode.StudyMode;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.repository.studymode.PersonalDataRepository;
import com.forgqi.resourcebaseserver.service.StudyModeService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class OtherController {
    private final StudyModeService studyModeService;
    private final PersonalDataRepository personalDataRepository;
    private final UserRepository userRepository;

    @PostMapping(value = "/study-modes")
    public Optional<StudyMode> creat(Instant estimate) {
        return studyModeService.creat(estimate);
    }

    @DeleteMapping(value = "/study-modes/{id}")
    public void delete(@PathVariable Long id) {
        studyModeService.delete(id);
    }

    @PutMapping(value = "/study-modes/{id}")
    public Optional<StudyMode> completed(@PathVariable Long id) {
        return studyModeService.completed(id);
    }

    @GetMapping(value = "/study-modes/users")
    public Optional<List<StudyMode>> findUserData(String query, Integer lastFewDays, Instant start, Instant end) {
        ZoneId zone = ZoneId.systemDefault();
        Optional<LocalDate> dateOptional = ParseUtil.parse(query, lastFewDays);
        if (dateOptional.isPresent()) {
            LocalDate date = dateOptional.get();
            return studyModeService.findPersonalData(Instant.from(date.atStartOfDay(zone)), Instant.now());
        }
        return studyModeService.findPersonalData(start, end);
    }

    @GetMapping(value = "/study-modes/users/profile")
    public Optional<PersonalData> findPersonalData() {
        return studyModeService.findPersonalData();
    }

    @GetMapping(value = "/study-modes/users/{id}")
    public Optional<PersonalData> findPersonalData(@PathVariable Long id) {
        return personalDataRepository.findById(id);
    }

    @Transactional
    @GetMapping(value = "/statistic/users")
    public Map<String, Long> getTotalUsers() {
        try (Stream<GradeOnly> stream = userRepository.findAllBy()) {
            return stream.collect(Collectors.groupingBy(gradeOnly -> gradeOnly.getDetail().getGrade() == null ? "unknown" : gradeOnly.getDetail().getGrade(), Collectors.counting()));
        }
//        return userRepository.count();
    }
}

