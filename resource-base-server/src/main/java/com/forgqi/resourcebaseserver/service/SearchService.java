package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.search.Searchable;
import com.forgqi.resourcebaseserver.repository.elasticsearch.SearchableRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.PostRepository;
import com.forgqi.resourcebaseserver.service.dto.util.ConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService implements InitializingBean {
    private final SearchableRepository searchableRepository;
    private final PostRepository postRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Async
    public void save(Searchable searchable) {
        searchableRepository.save(searchable);
    }

    public List<String> suggest(String query) {
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("title").prefix(query).skipDuplicates(true);
        var suggestion = elasticsearchOperations.suggest(new SuggestBuilder().addSuggestion("search-suggest", completionSuggestionBuilder), IndexCoordinates.of("searchable"))
                .getSuggest().getSuggestion("search-suggest");
        return suggestion.getEntries().stream().map(Suggest.Suggestion.Entry::getOptions).flatMap(options -> options.stream().map(option -> option.getText().string())).collect(Collectors.toList());
    }

    public SearchHits<Searchable> find(String query) {
        return searchableRepository.findByContent(query);
    }

    public SearchHits<Searchable> search(String query, Pageable pageable) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("content", query))
                .withPageable(pageable)
                .withHighlightBuilder(new HighlightBuilder().preTags("<mark>").postTags("</mark>"))
                .withHighlightFields(new HighlightBuilder.Field("content"))
                .build();
        return elasticsearchOperations.search(searchQuery, Searchable.class, IndexCoordinates.of("searchable"));
//        System.out.println(searchQuery.getQuery());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (searchableRepository.count() != 0) {
            return;
        }
        List<Post> postList = postRepository.findAll();
        Iterable<Searchable> searchableIterable = searchableRepository.saveAll(postList.stream().map(ConvertUtil::convertToSearchable).collect(Collectors.toList()));
        log.info("export {} post", searchableIterable.spliterator().getExactSizeIfKnown());
    }
}
