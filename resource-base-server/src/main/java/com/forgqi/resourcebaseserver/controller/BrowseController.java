package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.errors.NonexistenceException;
import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.common.util.ThreadLocalUtil;
import com.forgqi.resourcebaseserver.entity.Notice.Advice;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.entity.search.Searchable;
import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.repository.jpa.forum.CommentRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.PostRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.ReplyRepository;
import com.forgqi.resourcebaseserver.repository.jpa.notice.AdviseRepository;
import com.forgqi.resourcebaseserver.repository.jpa.studymode.PersonalDataRepository;
import com.forgqi.resourcebaseserver.repository.redis.MonthRepository;
import com.forgqi.resourcebaseserver.repository.redis.WeekRepository;
import com.forgqi.resourcebaseserver.service.SearchService;
import com.forgqi.resourcebaseserver.service.StudyModeService;
import com.forgqi.resourcebaseserver.service.UserService;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import com.forgqi.resourcebaseserver.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.util.CastUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BrowseController {
    private final PostServiceImpl postService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final StudyModeService studyModeService;
    private final PersonalDataRepository personalDataRepository;
    private final MonthRepository monthRepository;
    private final WeekRepository weekRepository;
    private final AdviseRepository adviseRepository;
    private final SearchService searchService;

    @GetMapping(value = "/search")
    public SearchHits<Searchable> search(String q, @PageableDefault Pageable pageable) {
        return searchService.search(q, pageable);
//        return searchService.find(q);
    }

    @GetMapping(value = "/suggest")
    public List<String> suggest(String q) {
        return searchService.suggest(q);
    }

    //    @SuppressWarnings("unchecked")
    @GetMapping(value = "/study-modes")
    public Map<?, ?> group(String query, Integer lastFewDays, Instant start, Instant end) {
        ZoneId zone = ZoneId.systemDefault();
        Optional<LocalDate> dateOptional = ParseUtil.parse(query, lastFewDays);
        if (dateOptional.isPresent()) {
            LocalDate date = dateOptional.get();
            Map<Long, Map<String, ?>> map = studyModeService.aggregationTotalTime(Instant.from(date.atStartOfDay(zone)), Instant.now());
            if (query != null) {
                var crudRepository = "thisWeek".equals(query) ? weekRepository : monthRepository;
                return map.entrySet().parallelStream()
                        .map(longMapEntry -> crudRepository.findById(longMapEntry.getKey())
                                .map(v -> {
                                    Map<String, Object> m = new HashMap<>(longMapEntry.getValue());
                                    m.put("like", v.getUpVote());
                                    return Map.entry(longMapEntry.getKey(), m);
                                }).orElse(Map.entry(longMapEntry.getKey(), CastUtils.cast(longMapEntry.getValue()))))
                        .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            return map;
        }
        return studyModeService.aggregationTotalTime(start, end);
    }

    @GetMapping(value = "/study-modes/rank")
    public Page<PersonalData> rank(@PageableDefault(sort = {"totalTime"}, size = 50, direction = Sort.Direction.DESC) Pageable pageable) {
        return personalDataRepository.findAll(pageable);
    }

    @PostMapping(value = "/registry")
    public User register(@RequestParam(defaultValue = "STUDENT") String type) {
        return userService.registerUser(ThreadLocalUtil.get(), type);
    }

    @GetMapping(value = {"/posts/subjects/{subject}", "/posts/subjects"})
    public Page<IPostDTO> getPage(@PathVariable(required = false) String subject,
                                  @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(required = false, defaultValue = "false") Boolean sticky) {
        if (subject == null) {
            return postRepository.findAllBySticky(sticky, pageable);
        }
        return postRepository.findBySubjectEqualsAndSticky(subject, sticky, pageable);
    }

    @GetMapping(value = "/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        postService.changeNumSize(id, "Pv");
        return postRepository.findById(id).orElseThrow(() -> new NonexistenceException("帖子不存在"));
    }

    @GetMapping(value = "/posts/{postId}/comments")
    public Page<Comment> getComments(@PathVariable Long postId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return commentRepository.findAllByPostEquals(postRepository.getOne(postId), pageable);
    }

    @GetMapping(value = "/comments/{commentId}/replies")
    public Page<Reply> getReplies(@PathVariable Long commentId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return replyRepository.findAllByCommentEquals(commentRepository.getOne(commentId), pageable);
    }

    @GetMapping(value = "/notice/{id}")
    public Optional<Advice> getNoticeById(@PathVariable Long id) {
        return adviseRepository.findById(id);
    }

    @GetMapping(value = "/notice")
    public Page<Advice> getNotices(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return adviseRepository.findAll(pageable);
    }

    @GetMapping(value = "/captcha")
    public Map<String, String> getCaptcha(@RequestHeader("User-Agent") String userAgent) {
        String captcha = userAgent.toLowerCase().split("/").length - 1 + "&" + LocalDate.now().getDayOfMonth();
        return Map.of("captcha", DigestUtils.md5DigestAsHex(captcha.toLowerCase().getBytes()));
    }
}
