package com.greenart.service;

import java.util.List;

import com.greenart.mapper.RegionalInfoMapper;
import com.greenart.vo.VaccineWeeksVO;
import com.greenart.vo.CoronaInfoVO;
import com.greenart.vo.CoronaSidoInfoVO;
import com.greenart.vo.CoronaVaccineInfoVO;
import com.greenart.vo.CoronaWeeksVO;

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
    public CoronaVaccineInfoVO selectCoronaVaccineInfo(String region, String date) {
        return mapper.selectCoronaVaccineInfo(region, date);
    }
    public String selectDangerAge(String date) {
        return mapper.selectDangerAge(date);
    }
    public List<CoronaWeeksVO> selectRegionalCoronaTwoWeeks(String region, String date){
        return mapper.selectRegionalCoronaTwoWeeks(region, date);
    }
    public List<VaccineWeeksVO> selectRegionalVaccineTwoWeeks(String region, String date){
        return mapper.selectRegionalVaccineTwoWeeks(region, date);
    }
    public List<VaccineWeeksVO> selectVaccineInfo(String date) {
        return mapper.selectVaccineInfo(date);
    }
}
