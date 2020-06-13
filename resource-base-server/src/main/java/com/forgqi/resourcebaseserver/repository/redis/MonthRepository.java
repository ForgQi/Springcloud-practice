package com.forgqi.resourcebaseserver.repository.redis;

import com.forgqi.resourcebaseserver.entity.studymode.MonthRank;
import org.springframework.data.repository.CrudRepository;

public interface MonthRepository extends CrudRepository<MonthRank, Long> {
}
