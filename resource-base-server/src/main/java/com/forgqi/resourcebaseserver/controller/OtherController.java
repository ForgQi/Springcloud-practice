package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.entity.studymode.StudyMode;
import com.forgqi.resourcebaseserver.service.StudyModeService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class OtherController {
    private final StudyModeService studyModeService;

    public OtherController(StudyModeService studyModeService) {
        this.studyModeService = studyModeService;
    }

    @PostMapping(value = "/study-modes")
    Optional<StudyMode> creat(Instant estimate) {
        return studyModeService.creat(estimate);
    }

    @DeleteMapping(value = "/study-modes/{id}")
    void delete(@PathVariable Long id) {
        studyModeService.delete(id);
    }

    @PutMapping(value = "/study-modes/{id}")
    Optional<StudyMode> completed(@PathVariable Long id) {
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
}
