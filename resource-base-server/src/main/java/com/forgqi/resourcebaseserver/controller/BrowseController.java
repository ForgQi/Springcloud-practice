package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.errors.NonexistenceException;
import com.forgqi.resourcebaseserver.common.util.ParseUtil;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Comment;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.entity.forum.Reply;
import com.forgqi.resourcebaseserver.entity.studymode.PersonalData;
import com.forgqi.resourcebaseserver.repository.studymode.MonthRepository;
import com.forgqi.resourcebaseserver.repository.studymode.PersonalDataRepository;
import com.forgqi.resourcebaseserver.repository.forum.CommentRepository;
import com.forgqi.resourcebaseserver.repository.forum.PostRepository;
import com.forgqi.resourcebaseserver.repository.forum.ReplyRepository;
import com.forgqi.resourcebaseserver.repository.studymode.WeekRepository;
import com.forgqi.resourcebaseserver.service.StudyModeService;
import com.forgqi.resourcebaseserver.service.UserService;
import com.forgqi.resourcebaseserver.service.dto.IPostDTO;
import com.forgqi.resourcebaseserver.service.dto.UsrPswDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BrowseController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final StudyModeService studyModeService;
    private final PersonalDataRepository personalDataRepository;
    private final MonthRepository monthRepository;
    private final WeekRepository weekRepository;

    @SuppressWarnings("unchecked")
    @GetMapping(value = "/study-modes")
    public Map<?, ?> group(String query, Integer lastFewDays, Instant start, Instant end) {
        ZoneId zone = ZoneId.systemDefault();
        Optional<LocalDate> dateOptional = ParseUtil.parse(query, lastFewDays);
        if (dateOptional.isPresent()) {
            LocalDate date = dateOptional.get();
            Map<Long, Map<String, ?>> map = studyModeService.aggregationTotalTime(Instant.from(date.atStartOfDay(zone)), Instant.now());
            if (query != null){
                var crudRepository = "thisWeek".equals(query) ? weekRepository : monthRepository ;
                return map.entrySet().parallelStream()
                        .map(longMapEntry -> crudRepository.findById(longMapEntry.getKey())
                                .map(v -> {
                                    Map<String, Object> m = new HashMap<>(longMapEntry.getValue());
                                    m.put("like", v.getUpVote());
                                    return Map.entry(longMapEntry.getKey(), m);
                                }).orElse(Map.entry(longMapEntry.getKey(), (Map<String, Object>)longMapEntry.getValue())))
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
    public User register(@RequestBody UsrPswDTO usrPswDTO, String type) throws IOException {
        //        UserDTO userDTO = new UserDTO().convertFor(user);
//        userDTO.setTokenDTO(authorizationFeignClient.getToken(loginDTO.convertToTokenMap()));

        return userService.registerUser(usrPswDTO, type);
    }

    @GetMapping(value = {"/posts/subjects/{subject}", "/posts/subjects"})
    public Page<IPostDTO> getPage(@PathVariable(required = false) String subject, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (subject == null){
            return postRepository.findAllBy(pageable);
        }
        return postRepository.findBySubjectEquals(subject, pageable);
    }

    @GetMapping(value = "/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postRepository.findById(id).map(post -> {
            post.setPv(post.getPv() + 1);
            return postRepository.save(post);
        }).orElseThrow(() -> new NonexistenceException("帖子不存在"));
    }

    @GetMapping(value = "/posts/{postId}/comments")
    public Page<Comment> getComments(@PathVariable Long postId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return commentRepository.findAllByPostEquals(postRepository.getOne(postId), pageable);
    }

    @GetMapping(value = "/comments/{commentId}/replies")
    public Page<Reply> getReplies(@PathVariable Long commentId, @PageableDefault(sort = {"createdDate"}) Pageable pageable) {
        return replyRepository.findAllByCommentEquals(commentRepository.getOne(commentId), pageable);
    }
}
