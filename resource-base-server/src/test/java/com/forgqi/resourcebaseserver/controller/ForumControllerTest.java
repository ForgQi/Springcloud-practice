package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 当一个类用 @RunWith 注释或继承一个用 @RunWith 注释的类时，
// JUnit 将调用它所引用的类来运行该类中的测试而不是开发者再去 JUnit 内部去构建它。
@RunWith(SpringRunner.class)
@SpringBootTest
//用来注入request
@WebAppConfiguration
//@AutoConfigureMockMvc
@ContextConfiguration
@Slf4j
class ForumControllerTest {
    //    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @BeforeEach //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();  //初始化MockMvc对象
    }

    //    @WithMockUser(value = "320160936051", username="320160936051",roles={"USER","ADMIN"})
//    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsService")
    @TestUtil.WithMockCustomUser
    @Test
    void update() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("content", "111111111111");
        System.out.println(map.toString());
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        User principal = new User();
//        principal.setId(320160636035L);
//        principal.setRoles(Stream.of("ADMIN").map(SysRole::new).collect(Collectors.toUnmodifiableList()));
//        Authentication auth =
//                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
//        context.setAuthentication(auth);

        String responseString = mockMvc.perform(
                //请求的url,请求的方法是get
                put("/v1/posts/5")
//                        .with(securityContext(context))
//                        .header("Authorization", "bearer " + "975584ee-ee57-4819-982d-dac530037ab5")
                        //数据的格式
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(map))
//                        .params(map)  //添加参数
        )
                .andExpect(status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);   //将相应的数据转换为字符串
        log.info(responseString);
    }
}