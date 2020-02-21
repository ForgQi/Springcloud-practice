package com.forgqi.resourcebaseserver.client;

import com.forgqi.resourcebaseserver.client.parse.Config;
import com.forgqi.resourcebaseserver.service.dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MyLzuFeignClient", url = "http://my.lzu.edu.cn", configuration = Config.Http2ClientConfig.class)
public interface MyLzuFeignClient {
    @PostMapping(value = "/getUser")
    ResultDTO<ResultDTO.MyLzuDTO> getUser(@RequestParam Long t);
}
