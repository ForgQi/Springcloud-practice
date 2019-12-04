package com.forgqi.resourcebaseserver.entity.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgqi.resourcebaseserver.entity.AbstractAuditingEntity;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Reply extends AbstractAuditingEntity implements IForum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(name = "id", nullable = false)
    private Long id;
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100) // 映射为字段，值不能为空
    @Audited
    private String content;
    private Long toUser;
    private String imageUrl;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "user_id")//设置在post表中的关联字段(外键)
    private User user;//所属用户
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "comment_id")//设置在comment表中的关联字段(外键)
    private Comment comment;//所属评论
}
