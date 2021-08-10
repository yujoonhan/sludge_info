package com.greenart.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CoronaInfoSidoVo {
    private Integer seq;
    private Integer deathCnt;
    private Integer defCnt;
    private Integer gubun;
    private Integer incDec;
    private Integer creisolClearCntateDt;
    private Integer isolIngCnt;
    private Integer localOccCnt;
    private Integer overFlowCnt;
    private Date createDt;
}
