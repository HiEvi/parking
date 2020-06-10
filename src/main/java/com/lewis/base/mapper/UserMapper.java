package com.lewis.base.mapper;

import com.lewis.base.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User login(Map<String, Object> map);

    User checkUserByAccount(String account);

    int updateIdCardByPrimaryKey(User user);

    int updatePlateNumByPrimaryKey(User User);

    int updateTypeByPrimaryKey(User record);

    int updateUserInfoById(User user);

    int insert(User record);

    List<User> selectAllByType(String type);

    User selectByAccount(String account);

    int selectCountByType(String type);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}