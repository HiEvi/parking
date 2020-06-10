package com.lewis.base.mapper;

import com.lewis.base.dto.NearLocationSearchParam;
import com.lewis.base.dto.ParkInfo;
import com.lewis.base.entity.Park;

import java.util.List;

public interface ParkMapper {

    List<ParkInfo> selectNearbyParksByLocation(NearLocationSearchParam param);

    Park selectParkByName(String name);

    int insert(Park park);

    Park selectByPrimaryKey(Integer id);

    List<String> selectParkNameByAccount(String account);

    List<Park> selectParkListById(Integer id);

    int updateRemainNumByPrimaryKey(Park record);

    int updateByName(Park record);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Park record);

    int updateByPrimaryKeySelective(Park record);

    int updateByPrimaryKey(Park record);
}