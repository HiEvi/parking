package com.lewis.base.controller;

import com.lewis.base.dto.ComResult;
import com.lewis.base.dto.NearLocationSearchParam;
import com.lewis.base.entity.Park;
import com.lewis.base.dto.ParkInfo;
import com.lewis.base.entity.User;
import com.lewis.base.mapper.ParkMapper;
import com.lewis.base.mapper.UserMapper;
import com.lewis.base.utils.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(value = "/park")
public class ParkController {
    @Autowired
    ParkMapper parkMapper;
    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/getNearbyParks",method = RequestMethod.POST)
    public ComResult<List<ParkInfo>> getNearbyParks(@RequestParam(value = "page",defaultValue="1") int page, @RequestParam(value = "limit",defaultValue="10") int limit, BigDecimal longitude,BigDecimal latitude) {
        ComResult<List<ParkInfo>> result = new ComResult<>();
        int size = 0;
        if (longitude != null && latitude != null) {

            BigDecimal[] around = DistanceUtils.getAround(latitude, longitude, 5000);
            NearLocationSearchParam param = new NearLocationSearchParam();
            param.setMinLat(around[0]);
            param.setMinLng(around[1]);
            param.setMaxLat(around[2]);
            param.setMaxLng(around[3]);
            param.setTargetLat(latitude);
            param.setTargetLng(longitude);
            param.setStart(page-1);
            param.setLimit(limit-1);

            List<ParkInfo> parkInfos = parkMapper.selectNearbyParksByLocation(param);

            size = parkInfos.size();
            if(size>0){
                result.setMsg("查找附近停车场成功");
            }else {
                result.setMsg("附近没有停车场");
            }
            result.setCode(0);
            result.setCount(size);
            result.setData(parkInfos);
        }else {
            result.setCode(1);
            result.setCount(size);
            result.setMsg("获取定位信息失败");
        }
        return result;
    }

    @RequestMapping(value = "/getParkInfo",method = RequestMethod.POST)
    public ComResult<Park> getParkInfo(int id){
        ComResult<Park> result = new ComResult<>();

        Park park = parkMapper.selectByPrimaryKey(id);

        if(park != null){
            result.setMsg("停车场信息查询成功");
            result.setData(park);
            result.setCode(0);
        }else {
            result.setMsg("停车场信息查询失败");
            result.setCode(1);
        }
        result.setCount(1);
        return result;
    }

    @RequestMapping(value = "/getParkInfoByName",method = RequestMethod.POST)
    public ComResult<Park> getParkInfoByName(String name){
        ComResult<Park> result = new ComResult<>();

        Park park = parkMapper.selectParkByName(name);

        if(park != null){
            result.setMsg("停车场信息查询成功");
            result.setData(park);
            result.setCode(0);
        }else {
            result.setMsg("停车场信息查询失败");
            result.setCode(1);
        }
        result.setCount(1);
        return result;
    }

    @RequestMapping(value = "/getAllParks",method = RequestMethod.POST)
    public ComResult<List<Park>> getAllParks(){
        ComResult<List<Park>> result = new ComResult<>();

        Integer id = 0;
        List<Park> parkList = parkMapper.selectParkListById(id);

        if(parkList != null){
            result.setMsg("停车场信息查询成功");
            result.setData(parkList);
            result.setCode(0);
        }else {
            result.setMsg("停车场信息查询失败");
            result.setCode(1);
        }
        result.setCount(parkList.size());
        return result;
    }

    @RequestMapping(value = "/delPark",method = RequestMethod.POST)
    public ComResult  delUser(Integer id){
        int i = parkMapper.deleteByPrimaryKey(id);
        ComResult result = new ComResult<>();

        if (i==1){
            result.setMsg("success");
        }else {
            result.setMsg("faile");
        }
        result.setCode(0);

        return result;
    }

    @RequestMapping(value = "/addPark",method = RequestMethod.POST)
    public ComResult addPark(@RequestBody Park park){
        ComResult<Park> result = new ComResult<>();
        //检查是否有这个停车场
        Park park1 = parkMapper.selectParkByName(park.getName());

        if(park1 != null){
            result.setMsg("已经存在该停车场名字");
            result.setCode(1);
        }else {
            //添加停车场
            int i = parkMapper.insert(park);
            User user = userMapper.checkUserByAccount(park.getAccount());
            user.setType("商家");
            int j = userMapper.updateTypeByPrimaryKey(user);

            if (i == 1 && j==1) {
                result.setMsg("停车场添加成功");
                result.setCode(0);
            } else {
                result.setMsg("停车场添加失败");
                result.setCode(1);
            }
        }
        return result;
    }

    @RequestMapping(value = "/updataPark",method = RequestMethod.POST)
    public ComResult updataPark(@RequestBody Park park){
        ComResult<Park> result = new ComResult<>();
        //检查是否有这个停车场
        Park park1 = parkMapper.selectParkByName(park.getName());

        if(park1 == null){
            result.setMsg("不存在该停车场名字");
            result.setCode(1);
        }else {
            //添加停车场
            int i = parkMapper.updateByName(park);

            if (i == 1) {
                result.setMsg("停车场添加成功");
                result.setCode(0);
            } else {
                result.setMsg("停车场添加失败");
                result.setCode(1);
            }
        }
        return result;
    }

    @RequestMapping(value = "/getParkInfoByAccount",method = RequestMethod.POST)
    public ComResult<List<String>> getParkInfoByAccount(String account){
        ComResult<List<String>> result = new ComResult<>();

        List<String> parkList = parkMapper.selectParkNameByAccount(account);

        if(parkList != null){
            result.setMsg("停车场信息查询成功");
            result.setData(parkList);
            result.setCode(0);
        }else {
            result.setMsg("停车场信息查询失败");
            result.setCode(1);
        }
        result.setCount(1);
        return result;
    }
}
