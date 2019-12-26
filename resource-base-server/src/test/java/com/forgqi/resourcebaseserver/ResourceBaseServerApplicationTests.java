package com.forgqi.resourcebaseserver;

import com.forgqi.resourcebaseserver.common.Voted;
import com.forgqi.resourcebaseserver.common.errors.InvalidPasswordException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.*;

import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@RunWith(SpringRunner.class)
@SpringBootTest
//用来注入request
@WebAppConfiguration
public class ResourceBaseServerApplicationTests {
    @Autowired
    MockHttpServletRequest request;

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
}
