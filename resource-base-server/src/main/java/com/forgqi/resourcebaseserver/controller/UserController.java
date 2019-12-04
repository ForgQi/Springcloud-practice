package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.Notice;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.forum.Vote;
import com.forgqi.resourcebaseserver.repository.NoticeRepository;
import com.forgqi.resourcebaseserver.repository.UserRepository;
import com.forgqi.resourcebaseserver.repository.VoteRepository;
import com.forgqi.resourcebaseserver.service.UserService;
import com.forgqi.resourcebaseserver.service.dto.Editable;
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
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @PutMapping(value = "/editable")
    public Optional<User> change(Editable editable) {
        return userService.changeAvatarOrNickName(editable);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
//    public String home(@RequestParam(value = "name", defaultValue = "World") String name)
    public User role(@PathVariable Long id, @RequestBody List<String> sysRoles) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
//        String name = userDetails.getUsername();
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

    @GetMapping(value = "/profile")
    public Optional<User> getProfile() {
        return userService.findUserBySecurityContextFormRepository();
    }

    @GetMapping(value = "/notice")
    @Cacheable(cacheNames = {"notice"}, key = "'notify'")
    public Optional<Notice> getNotice() {
        return noticeRepository.findFirstByOrderByIdDesc();
    }

    @CacheEvict(cacheNames = {"notice"}, key = "'notify'")
    @PostMapping(value = "/notice")
    public Notice pushNotice(@RequestBody Notice notice) {
        return noticeRepository.save(notice);
    }
}
