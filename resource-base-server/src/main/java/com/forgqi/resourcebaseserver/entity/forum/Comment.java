package com.forgqi.resourcebaseserver.entity.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgqi.resourcebaseserver.entity.AbstractAuditingEntity;
import com.forgqi.resourcebaseserver.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
public class Comment extends AbstractAuditingEntity implements IVoteEntity, IForum<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2, max = 250)
    @Column(nullable = false, length = 250) // 映射为字段，值不能为空
    @Audited
    private String content;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
//可选属性optional=false,表示user不能为空。删除评论，不影响用户
    @JoinColumn(name = "user_id")//设置在post表中的关联字段(外键)
    private User user;//所属用户
    private Long toUser;
    private String imageUrl;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    // 可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "post_id")//设置在post表中的关联字段(外键)
    private Post post;//所属帖子
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除帖子，会级联删除该用户的所有评论
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="comment"中的comment是Reply中的comment属性
    @Where(clause = "")
//    @BatchSize(size = 2) 仅改变每次处理的大小而不是总大小
    private List<Reply> replyList;//回复列表
    @Column(nullable = false)
    private Integer upVote = 0;
    @Column(nullable = false)
    private Integer downVote = 0;

    @Version
    private Long version;

    public Comment() {
    }

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, Long version) {
        this.id = id;
        this.version = version;
    }
}
