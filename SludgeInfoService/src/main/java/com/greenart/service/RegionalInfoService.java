package com.greenart.service;

import com.greenart.mapper.RegionalInfoMapper;
import com.greenart.vo.CoronaInfoVO;
import com.greenart.vo.CoronaSidoInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionalInfoService {
    @Autowired
    RegionalInfoMapper mapper;
    public CoronaSidoInfoVO selectRegionalCoronaInfo(String region, String date) {
        return mapper.selectRegionalCoronaInfo(region, date);
    }
    public CoronaInfoVO selectCoronaInfoRegionalTotal(String date){
        return mapper.selectCoronaInfoRegionalTotal(date);
    }
}
