package com.forgqi.resourcebaseserver.repository.elasticsearch;

import com.forgqi.resourcebaseserver.entity.search.Searchable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.repository.CrudRepository;

public interface SearchableRepository extends CrudRepository<Searchable, Long> {
    SearchHits<Searchable> findByContent(String content);
}
