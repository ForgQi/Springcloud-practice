package com.forgqi.resourcebaseserver.entity.search;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;

import javax.persistence.Id;

@Data
@Document(indexName = "searchable")
public class Searchable {
    @Id
    private String id;

    private Long searchableId;
    private String type;

    @CompletionField
    private Completion title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

}
