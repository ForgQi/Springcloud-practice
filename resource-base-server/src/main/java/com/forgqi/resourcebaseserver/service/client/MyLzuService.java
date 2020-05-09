package com.forgqi.resourcebaseserver.service.client;

import com.forgqi.resourcebaseserver.client.MyLzuFeignClient;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.service.dto.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MyLzuService {
    private final MyLzuFeignClient myLzuFeignClient;

    public User getUser() {
        ResultDTO<ResultDTO.MyLzuDTO> resultDTO = myLzuFeignClient.getUser(Instant.now().toEpochMilli());
        return resultDTO.getData().convertToUser();
    }
}
