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
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class Post extends AbstractAuditingEntity implements IVoteEntity, IForum<List<String>> {
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    @Column(nullable = false)
    private Long id;

    //    @NotEmpty(message = "标题不能为空")
    @Size(min = 2, max = 50)
    @Column(length = 50) // 映射为字段，值不能为空
    private String title;
    @Lob  // 大对象，映射 MySQL 的 Long Text 类型
    @Basic(fetch = FetchType.LAZY) // 懒加载
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false) // 映射为字段，值不能为空
    @Audited
    private String content;//文章全文内容

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    // 可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "user_id")//设置在post表中的关联字段(外键)
    private User user;//所属用户
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="post"中的post是comment中的post属性
    private List<Comment> commentList;//评论列表
    private Integer commentSize = 0;

    private String subject;
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100) // 映射为字段，值不能为空
    private String summary;

    @Column(nullable = false)
    private boolean anonymous = false;
    @Audited
    @Column(nullable = false)
    private boolean sticky = false;
    @Audited
    @Column(nullable = false)
    private boolean highlight = false;
    @ElementCollection
    private List<String> imageUrl;
    @ElementCollection
    private List<String> topic;
    @ElementCollection
    private Map<String, String> profile;

    @Column(nullable = false)
    private Integer upVote = 0;
    @Column(nullable = false)
    private Integer downVote = 0;
    @Column(nullable = false)
    private Integer pv = 0;

    @Version
    private Long version;

    public Post() {
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post(Long id, Long version) {
        this.id = id;
        this.version = version;
    }
}
