package com.forgqi.resourcebaseserver.service.dto;

import com.forgqi.resourcebaseserver.entity.User;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer state;
    private String msg;
    private T data;

    // 学校的接口命名难以理解
    @Data
    public static class MyLzuDTO {
        private String csrq;
        private String dwmc;
        private String xmpy;
        private String dqzt;
        private String xb;
        private String txdz;
        private String mz;
        private String xxl;
        private String sxzy;
        private String hyzk;
        private String xm;
        private String xxw;
        private String yddh;
        private String byyx;
        private String rybh;
        private String zzmm;
        private String dzxx;
        private String rn;
        private String jg;

        public User convertToUser() {
            return UsrInfoDTO.builder()
                    .id(Long.parseLong(rybh))
                    .classNo(txdz)
                    .college(dwmc)
                    .education(dqzt)
                    .grade(xxl)
                    .name(xm)
                    .subject(sxzy)
                    .build().convertToUser();
        }
    }
}
