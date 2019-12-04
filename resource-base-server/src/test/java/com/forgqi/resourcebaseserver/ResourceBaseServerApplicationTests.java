package com.forgqi.resourcebaseserver;

import com.forgqi.resourcebaseserver.common.errors.InvalidPasswordException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
//用来注入request
@WebAppConfiguration
public class ResourceBaseServerApplicationTests {
    @Autowired
    MockHttpServletRequest request;

    @Test
    public void contextLoads() {
        System.out.println(new InvalidPasswordException("").create(request));
    }
}
