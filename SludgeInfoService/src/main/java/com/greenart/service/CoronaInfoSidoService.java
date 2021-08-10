package com.greenart.service;

import com.greenart.mapper.CoronaInfoSidoMapper;
import com.greenart.vo.CoronaInfoSidoVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoronaInfoSidoService {
    @Autowired
    CoronaInfoSidoMapper mapper;
    public void insertCoronaInfoSido(CoronaInfoSidoVo vo){
        mapper.insertCoronaInfoSido(vo);
    }
}
