package com.greenart.mapper;

import com.greenart.vo.CoronaInfoVO;
import com.greenart.vo.CoronaSidoInfoVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionalInfoMapper {
    public CoronaSidoInfoVO selectRegionalCoronaInfo(String region, String date);
    public CoronaInfoVO selectCoronaInfoRegionalTotal(String date);
}
