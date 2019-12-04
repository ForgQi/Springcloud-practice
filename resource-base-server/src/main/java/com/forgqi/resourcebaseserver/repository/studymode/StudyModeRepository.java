package com.forgqi.resourcebaseserver.repository.studymode;

import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.entity.studymode.StudyMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface StudyModeRepository extends JpaRepository<StudyMode, Long> {
    List<StudyMode> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    List<StudyMode> findAllByCreatedDateBetweenAndPersonalData(Instant startDate, Instant endDate, PersonalData id);
}
