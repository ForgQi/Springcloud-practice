package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.Advise;
import com.forgqi.resourcebaseserver.entity.Notice;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import com.forgqi.resourcebaseserver.repository.AdviseRepository;
import com.forgqi.resourcebaseserver.repository.NoticeRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.repository.VoteRepository;
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

    @PutMapping(value = "/editable")
    public Optional<User> change(Editable editable) {
        return userService.changeAvatarOrNickName(editable);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public User role(@PathVariable Long id, @RequestBody List<String> sysRoles) {
        return userService.reloadUserFromSecurityContext(id, sysRoles);
    }

    @GetMapping(value = "/users/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping(value = "/users/votes")
    public Optional<Page<Vote>> getVote(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return UserHelper.getUserBySecurityContext()
                .flatMap(user -> voteRepository.findByUserId(user.getId(), pageable));
    }

    @GetMapping(value = "/Notification")
    public Page<Notice> getNotification(
            @RequestBody List<String> registrationTokens,
            @RequestParam(defaultValue = "")String notificationChannel,
            @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        if (!notificationChannel.isBlank()){
            return noticeRepository.findAllByNotificationChannel(notificationChannel, pageable);
        }
        return noticeRepository.findAllByRegistrationTokensIn(registrationTokens, pageable);
    }

    @GetMapping(value = "/Notification/{id}")
    public Optional<Notice> findNotification(@PathVariable Long id) {
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
