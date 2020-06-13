package com.forgqi.resourcebaseserver.repository.jpa.studymode;

import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
}
