package com.lewis.base.mapper;

import com.lewis.base.dto.OrderParam;
import com.lewis.base.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    int insert(Order record);

    Order selectOrderByName(Map<String,Object> map);

    List<Order> selectOrderListById(Integer id);

    List<Order> selectOrderByNames(OrderParam param);

    List<Order> selectCarOrderByNames(OrderParam param);

    Order selectCurOrderByName(Map<String,Object> map);

    int updateStartStayByPrimaryKey(Order order);

    int updateStatusByPrimaryKey(Order record);

    int deleteByPrimaryKey(Integer id);


    int updateRemainNumByPrimaryKey(Order order);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

}