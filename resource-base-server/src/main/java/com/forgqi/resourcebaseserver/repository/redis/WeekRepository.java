package com.forgqi.resourcebaseserver.repository.redis;

import com.forgqi.resourcebaseserver.entity.studymode.WeekRank;
import org.springframework.data.repository.CrudRepository;

public interface WeekRepository extends CrudRepository<WeekRank, Long> {
}
