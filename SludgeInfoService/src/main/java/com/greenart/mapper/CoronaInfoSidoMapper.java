package com.greenart.mapper;

import com.greenart.vo.CoronaInfoSidoVo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoronaInfoSidoMapper {
    public void insertCoronaInfoSido(CoronaInfoSidoVo vo);
}
