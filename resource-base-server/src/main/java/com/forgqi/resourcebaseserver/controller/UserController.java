package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.Notice.Advise;
import com.forgqi.resourcebaseserver.entity.Notice.Notice;
import com.forgqi.resourcebaseserver.entity.Notice.UserNoticeState;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.repository.VoteRepository;
import com.forgqi.resourcebaseserver.repository.forum.PostRepository;
import com.forgqi.resourcebaseserver.repository.notice.AdviseRepository;
import com.forgqi.resourcebaseserver.repository.notice.NoticeRepository;
import com.forgqi.resourcebaseserver.repository.notice.UserNoticeStateRepository;
import com.forgqi.resourcebaseserver.service.UserService;
import com.forgqi.resourcebaseserver.service.dto.Editable;
import com.forgqi.resourcebaseserver.service.dto.IUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdviseRepository adviseRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final NoticeRepository noticeRepository;
    private final PostRepository postRepository;
    private final UserNoticeStateRepository userNoticeStateRepository;

    @PutMapping(value = "/editable")
    public Optional<User> change(Editable editable) {
        return userService.changeAvatarOrNickName(editable);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public User role(@PathVariable Long id, @RequestBody List<String> sysRoles) {
        return userService.reloadUserFromSecurityContext(id, sysRoles);
    }

    @GetMapping(value = {"/users/{id}", "/users/{id}/{category}"})
    public Optional<?> getUser(@PathVariable Long id,
                               @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @PathVariable(required = false) String category) {
        if (category == null) {
            return userRepository.findById(id);
        } else if ("posts".equals(category)) {
            return postRepository.getAllByUserId(id, pageable);
        }
        return Optional.empty();
    }

    @GetMapping(value = "/users/votes")
    public Optional<Page<Vote>> getVote(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return UserHelper.getUserBySecurityContext()
                .flatMap(user -> voteRepository.findByUserId(user.getId(), pageable));
    }

    @GetMapping(value = "/Notification")
    public Page<Notice> getNotification(
            @RequestParam(required = false) List<String> registrationTokens,
            @RequestParam(required = false) List<String> notificationChannel,
            Long userId,
            @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (userId != null) {
            return noticeRepository.findAllByOriginalSourceUserId(userId, pageable).map(notice -> {
                if (!userNoticeStateRepository.existsById(new UserNoticeState.UserNotice(UserHelper.getUserIdBySecurityContext(), notice.getId()))) {
                    notice.setRead(false);
                }
                return notice;
            });
//            return noticeRepository.findAllByOriginalSourceUserId(userId, pageable);
        }
        return noticeRepository.findDistinctByRegistrationTokensInAndNotificationChannelIn(registrationTokens, notificationChannel, pageable).map(notice -> {
            if (!userNoticeStateRepository.existsById(new UserNoticeState.UserNotice(UserHelper.getUserIdBySecurityContext(), notice.getId()))) {
                notice.setRead(false);
            }
            return notice;
        });
//        return noticeRepository.findDistinctByRegistrationTokensInAndNotificationChannelIn(registrationTokens, notificationChannel, pageable);
    }

    @GetMapping(value = "/Notification/{id}")
    public Optional<Notice> findNotification(@PathVariable Long id) {
        UserHelper.getUserBySecurityContext().ifPresent(user -> {
            UserNoticeState userNoticeState = new UserNoticeState();
            userNoticeState.setId(new UserNoticeState.UserNotice(user.getId(), id));
            userNoticeStateRepository.save(userNoticeState);
        });
        return noticeRepository.findById(id).map(notice -> {
            notice.setRead(true);
            return noticeRepository.save(notice);
        });
    }

    @DeleteMapping(value = "/Notification/{id}")
    public void deleteNotification(@PathVariable Long id) {
        noticeRepository.deleteById(id);
    }

    @PostMapping(value = "/Notification")
    public Notice pushNotification(@RequestBody Notice notice) {
        return noticeRepository.save(notice);
    }

    @GetMapping(value = "/profile")
    public Optional<IUserDTO> getProfile() {
        return UserHelper.getUserBySecurityContext().flatMap(user -> userRepository.findUserById(user.getId()));
    }

    @GetMapping(value = "/notice")
    @Cacheable(cacheNames = {"notice"}, key = "'notify'")
    public Optional<Advise> getNotice() {
        return adviseRepository.findFirstByOrderByIdDesc();
    }

    @PostMapping(value = "/notice")
    @CacheEvict(cacheNames = {"notice"}, key = "'notify'")
    public Advise pushNotice(@RequestBody Advise advise) {
        return adviseRepository.save(advise);
    }
}
