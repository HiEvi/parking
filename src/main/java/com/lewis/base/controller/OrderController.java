package com.lewis.base.controller;

import com.lewis.base.dto.ComResult;
import com.lewis.base.dto.OrderParam;
import com.lewis.base.entity.*;
import com.lewis.base.mapper.OrderMapper;
import com.lewis.base.mapper.ParkMapper;
import com.lewis.base.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ParkMapper parkMapper;
    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/addPreOrder",method = RequestMethod.POST)
    public ComResult addPreOrder(String account, String name, Date preTime, String phone, String status) {
        ComResult result = new ComResult<>();
        Map<String,Object> map=new HashMap<>();

        map.put("account",account);
        map.put("name",name);
        map.put("status",status);

        Order order = orderMapper.selectOrderByName(map);
        if(order != null){
            result.setMsg("已经存在订单");
            result.setCode(1);
        }else{
            Order order1 = new Order();
            order1.setAccount(account);
            order1.setName(name);
            order1.setPreTime(preTime);
            order1.setPhone(phone);
            order1.setStatus(status);
            //获得停车位编号
            String parkUid = "a区49号";
            order1.setParkUid(parkUid);

            Park park1 = parkMapper.selectParkByName(order1.getName());

            if(park1 != null){
                if(park1.getRemainParkNum()>1){
                    int i = orderMapper.insert(order1);
                    park1.setRemainParkNum(park1.getRemainParkNum()-1);
                    int j = parkMapper.updateRemainNumByPrimaryKey(park1);
                    if (i==1 && j==1){
                        result.setMsg("预订成功");
                        result.setCode(0);
                        map.put("account",order1.getAccount());
                        map.put("name",order1.getName());
                        map.put("status",order1.getStatus());
                        order1 = orderMapper.selectOrderByName(map);
                        result.setData(order1);
                    }else {
                        result.setMsg("预订失败");
                        result.setCode(1);
                    }
                }else {
                    result.setMsg("车位已满，请换个时间预订");
                    result.setCode(1);
                }
            }else{
                result.setMsg("停车场信息查询失败");
                result.setCode(1);
            }
        }
        return result;
    }

    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
    public ComResult cancelOrder(String account, String name, String status) {
        ComResult result = new ComResult<>();
        Map<String,Object> map=new HashMap<>();

        map.put("account",account);
        map.put("name",name);
        map.put("status",status);

        Order order = orderMapper.selectOrderByName(map);
        Park park = parkMapper.selectParkByName(name);
        if(order != null && park != null){
            order.setStatus("交易取消");
            int i = orderMapper.updateStatusByPrimaryKey(order);
            park.setRemainParkNum(park.getRemainParkNum()+1);
            int j = parkMapper.updateRemainNumByPrimaryKey(park);
            if(i == 1 && j == 1) {
                result.setMsg("撤销成功");
                result.setCode(0);
            }
        }else{
            result.setMsg("撤销失败");
            result.setCode(1);
        }
        return result;
    }

    @RequestMapping(value = "/getParkOrder",method = RequestMethod.POST)
    public ComResult<List<Order>> getParkOrder(@RequestParam(value = "page",defaultValue="1") int page, @RequestParam(value = "limit",defaultValue="10") int limit, String account, int status) {
        ComResult<List<Order>> result = new ComResult<>();

//        User user = userMapper.checkUserByAccount(account);
        //一个用户可能有多个停车场
        List<String> parkList =parkMapper.selectParkNameByAccount(account);

        if (parkList != null) {

            OrderParam orderParam = new OrderParam();

            orderParam.setAccount(account);
            orderParam.setName(parkList);
            orderParam.setStart(page-1);
            orderParam.setLimit(limit-1);
            if(status == 0){
                orderParam.setStatus("交易成功");
            }else {
                orderParam.setStatus("正在交易");
            }

            List<Order> orderList = orderMapper.selectOrderByNames(orderParam);

            if(orderList.size() > 0){
                result.setData(orderList);
                result.setCode(0);
                result.setMsg("查询历史订单成功");
            }else {
                result.setMsg("没有更多内容了");
                result.setCode(0);
            }
            result.setCount(orderList.size());
        }else{
            result.setMsg("查询历史订单失败");
            result.setCode(1);
        }
        return result;
    }

    @RequestMapping(value = "/getMoney",method = RequestMethod.POST)
    public ComResult getMoney(String account) {

        ComResult result = new ComResult<>();
//        User user = userMapper.checkUserByAccount(account);
        List<String> parkList =parkMapper.selectParkNameByAccount(account);

        if (parkList != null) {
            OrderParam orderParam = new OrderParam();
            orderParam.setAccount(account);
            orderParam.setName(parkList);
            orderParam.setStart(0);
            orderParam.setLimit(100);
            orderParam.setStatus("交易成功");

            List<Order> orderList = orderMapper.selectOrderByNames(orderParam);

            BigDecimal num = new BigDecimal(0.00);
            if (orderList.size() > 0) {
                for (int i = 0; i < orderList.size(); i++) {
                    num = orderList.get(i).getMoney().add(num);
                }
                result.setMsg("查询收益成功");
            } else {
                result.setMsg("暂无收益");
            }
            result.setData(num);
            result.setCode(0);
            result.setCount(orderList.size());
        } else {
            result.setMsg("查询收益失败");
            result.setCode(1);
        }
        return result;
    }

    @RequestMapping(value = "/getCarOrder",method = RequestMethod.POST)
    public ComResult<List<Order>> getCarOrder(@RequestParam(value = "page",defaultValue="1") int page, @RequestParam(value = "limit",defaultValue="10") int limit, String account, int status) {

        ComResult<List<Order>> result = new ComResult<>();
        OrderParam orderParam = new OrderParam();

        orderParam.setAccount(account);
        orderParam.setStart(page-1);
        orderParam.setLimit(limit-1);
        if(status == 0){
            orderParam.setStatus("交易成功");
        }else if(status == 1){
            orderParam.setStatus("正在交易");
        }else{
            orderParam.setStatus("开始交易");
        }

        //User user = userMapper.checkUserByAccount(account);
//        List<String> parkList =parkMapper.selectParkNameByAccount(account);
//
//        if (parkList != null) {
//            OrderParam orderParam = new OrderParam();
//
//            orderParam.setAccount(account);
//            orderParam.setName(parkList);
//            orderParam.setStart(page-1);
//            orderParam.setLimit(limit-1);
//            if(status == 0){
//                orderParam.setStatus("交易成功");
//            }else if(status == 1){
//                orderParam.setStatus("正在交易");
//            }else{
//                orderParam.setStatus("开始交易");
//            }

            List<Order> orderList = orderMapper.selectCarOrderByNames(orderParam);

 //           System.out.println(orderList.get(0).getStartStay());
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            simpleDateFormat.format()


            if(orderList.size() > 0){
                result.setData(orderList);
                result.setCode(0);
                result.setMsg("查询历史订单成功");
            }else {
                result.setMsg("没有更多内容了");
                result.setCode(0);
            }
            result.setCount(orderList.size());
            return result;
    }

    @RequestMapping(value = "/getCurOrder",method = RequestMethod.POST)
    public ComResult<Order> getCurOrder(String account, String plateNum) {
        ComResult<Order> result = new ComResult<>();

        User user = userMapper.checkUserByAccount(account);

        if(user.getPlateNum().equals(plateNum)){

            Map<String,Object> map=new HashMap<>();
            map.put("account",account);
            map.put("status","正在交易");

            Order order = orderMapper.selectCurOrderByName(map);
            if(order!=null){
                Park park = parkMapper.selectParkByName(order.getName());
                Date startStay = order.getStartStay();
                Date curTime = new Date();
                long firstPrice = park.getFirstPrice().longValue();
                long laterPrice = park.getLaterPrice().longValue();
                long total = 0L;
                //计算时间差
                long diff = curTime.getTime() - startStay.getTime();
                //计算天数
                long days = diff / (1000 * 60 * 60 * 24);
                //计算小时
                long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                //计算分钟
                long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
                //计算秒
                long seconds = (diff % (1000 * 60)) / 1000;

                hours = days * 24 + hours;

                if(hours <= 0){
                    result.setMsg("未生成订单金额");
                    result.setCode(1);
                }
                if(minutes != 0){
                    hours = hours + 1;
                }
                if(hours > 1){
                    total = (hours - 1) * laterPrice + firstPrice;
                }else {
                    total = firstPrice;
                }

                result.setCount((int)total);
                result.setCode(0);
                result.setData(order);
                result.setMsg("当前有一个订单");
            }
        }else{
            result.setCode(1);
            result.setMsg("当前暂无订单");
        }
        return result;
    }

    @RequestMapping(value = "/getPayment",method = RequestMethod.POST)
    public ComResult getPayment(String account, String name) {

        ComResult result = new ComResult<>();
        Map<String,Object> map=new HashMap<>();

        map.put("account",account);
        map.put("name",name);
        map.put("status","正在交易");

        Park park = parkMapper.selectParkByName(name);
        Order order = orderMapper.selectOrderByName(map);

        Date startStay = order.getStartStay();
        Date curTime = new Date();

        long firstPrice = park.getFirstPrice().longValue();
        long laterPrice = park.getLaterPrice().longValue();
        long total = 0L;
        //计算时间差
        long diff = curTime.getTime() - startStay.getTime();
        //计算天数
        long days = diff / (1000 * 60 * 60 * 24);
        //计算小时
        long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        //计算分钟
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        //计算秒
        long seconds = (diff % (1000 * 60)) / 1000;

        hours = days * 24 + hours;

        if(hours <= 0){
            result.setMsg("未生成订单金额");
            result.setCode(1);
        }
        if(minutes != 0){
            hours = hours + 1;
        }
        if(hours > 1){
            total = (hours - 1) * laterPrice + firstPrice;
        }else {
            total = firstPrice;
        }
        BigDecimal total1 = new BigDecimal(total);

        order.setMoney(total1);
        order.setStatus("交易成功");
        order.setEndStay(curTime);
        order.setStayTime((int)hours);

        int i = orderMapper.updateStatusByPrimaryKey(order);
        park.setRemainParkNum(park.getRemainParkNum()+1);
        int j = parkMapper.updateRemainNumByPrimaryKey(park);
        if(i == 1 && j == 1){
            result.setCode(0);
            result.setMsg("支付成功，金额为"+total);
        }else{
            result.setCode(1);
            result.setMsg("支付失败");
        }
        return result;
    }

    @RequestMapping(value = "/startStay",method = RequestMethod.POST)
    public ComResult<Order> startStay(String account,String status) {
        ComResult result = new ComResult<>();
        Map<String,Object> map=new HashMap<>();

        map.put("account",account);
//        map.put("name",name);
        map.put("status",status);

        Order order = orderMapper.selectCurOrderByName(map);
        Date curTime = new Date();
        order.setStartStay(curTime);
        order.setStatus("正在交易");
        int i = orderMapper.updateStartStayByPrimaryKey(order);
        Park park = parkMapper.selectParkByName(order.getName());
        if(i == 1 && park != null){
            result.setCode(0);
            result.setData(park);
        }else{
            result.setMsg("数据库未访问到数据");
            result.setCode(1);
        }
        return result;
    }

    @RequestMapping(value = "/getAllOrders",method = RequestMethod.POST)
    public ComResult<List<Order>> getAllOrders(){
        ComResult<List<Order>> result = new ComResult<>();

        Integer id = 0;
        List<Order> orderList = orderMapper.selectOrderListById(id);

        if(orderList != null){
            result.setMsg("停车场信息查询成功");
            result.setData(orderList);
            result.setCode(0);
        }else {
            result.setMsg("停车场信息查询失败");
            result.setCode(1);
        }
        result.setCount(orderList.size());
        return result;
    }

    @RequestMapping(value = "/delOrder",method = RequestMethod.POST)
    public ComResult  delOrder(Integer id){
        int i = orderMapper.deleteByPrimaryKey(id);
        ComResult result = new ComResult<>();

        if (i==1){
            result.setMsg("success");
        }else {
            result.setMsg("faile");
        }
        result.setCode(0);

        return result;
    }

}
