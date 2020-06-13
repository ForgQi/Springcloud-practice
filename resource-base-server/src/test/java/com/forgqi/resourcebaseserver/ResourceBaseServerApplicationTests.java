package com.forgqi.resourcebaseserver;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.InvalidPasswordException;
import com.forgqi.resourcebaseserver.entity.forum.Post;
import com.forgqi.resourcebaseserver.repository.jpa.UserRepository;
import com.forgqi.resourcebaseserver.repository.jpa.forum.PostRepository;
import com.forgqi.resourcebaseserver.service.impl.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Arrays;

import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@RunWith(SpringRunner.class)
@SpringBootTest
//用来注入request
@WebAppConfiguration
@Slf4j
public class ResourceBaseServerApplicationTests {
    @Autowired
    MockHttpServletRequest request;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostServiceImpl postService;

    @Test
    public void contextLoads() {
        InvalidPasswordException invalidPasswordException = new InvalidPasswordException("");
        System.out.println(invalidPasswordException.create(request));
        System.out.println(Voted.State.valueOf("up".toUpperCase()));
        System.out.println(Voted.State.valueOf("down".toUpperCase()));

        LocalDate date1 = LocalDate.of(2019, 1, 15);
        LocalDate date2 = LocalDate.of(2019, 3, 10);
        Period period = Period.between(date1, date2);
        System.out.println(period.getYears() + "-" + period.getMonths() + "-" + period.getDays());
        LocalDate now = LocalDate.of(2019, 12, 16);
        LocalDate date = now.with(previous(DayOfWeek.MONDAY));
        System.out.println(date);
        LocalDate now1 = LocalDate.of(2019, 12, 16);
        LocalDate date0 = now1.with(previousOrSame(DayOfWeek.MONDAY));
        System.out.println(date0);
        System.out.println((double) 2 / 5);

        System.out.println(Instant.from(date.atStartOfDay(ZoneId.systemDefault())));

    }

    @Transactional
    public void retry() {
        new Thread(() -> {
            postRepository.findById(5L).ifPresent(post -> {
                post.setCommentSize(80);
                Post save = postRepository.save(post);
//                log.info("yes"+save.getVersion());
            });
        }).start();
    }

    @Test
    public void retryTest() throws InterruptedException {
        retry();
        postService.changeNumSize(5L, null);
        Thread.sleep(5000);
    }

    @Test
    public void postRepositoryTest() {
        log.info("oh,no!");
        postRepository.findByImageUrlIn(Arrays.asList("123", "12"), Pageable.unpaged())
                .forEach(iPostDTO -> System.out.println(iPostDTO.getId()));
        userRepository.findUserById(320160936051L).ifPresent(iUserDTO -> System.out.println(iUserDTO.isEnabled()));
    }
}
