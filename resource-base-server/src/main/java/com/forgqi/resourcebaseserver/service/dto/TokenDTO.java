package com.forgqi.resourcebaseserver.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenDTO {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;
    private String scope;
}
