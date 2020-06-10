package com.lewis.base.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lewis.base.dto.ComResult;
import com.lewis.base.dto.IdentityCard;
import com.lewis.base.entity.User;
import com.lewis.base.mapper.UserMapper;

import com.mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserMapper userMapper;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ComResult  login(String account,String pwd) throws Exception {
        Map<String,Object> map=new HashMap<>();
        ComResult result = new ComResult<>();

        map.put("account",account);
        map.put("pwd",pwd);

        //验证密码
        User user = userMapper.login(map);

        if (user==null){
            result.setCode(1);
            result.setMsg("账号或密码不正确");
        }else {
            result.setCode(0);
            result.setMsg("登录成功");
            result.setData(user);
        }
        return result;
    }

    //注册
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ComResult  register(String account,String pwd) throws Exception {
        ComResult result = new ComResult<>();
        //检查是否存在用户
        User user = userMapper.checkUserByAccount(account);

        if (user!=null){
            result.setCode(1);
            result.setMsg("账号已存在");
        }else {
            User user1 = new User();

            user1.setPwd(pwd);
            user1.setAccount(account);
            user1.setType("用户");
            //插入用户
            int i = userMapper.insert(user1);

            if (i==1){
                result.setCode(0);
                result.setMsg("注册成功");
                result.setData(user1);
            }else {
                result.setCode(1);
                result.setMsg("注册失败");
            }
        }
        return result;
    }

    //添加身份证
    @RequestMapping(value = "/addUserInfo",method = RequestMethod.POST)
    public ComResult  addUserInfo(@RequestBody IdentityCard identityCard) throws Exception {
        ComResult result = new ComResult<>();

        User user = userMapper.checkUserByAccount(identityCard.getAccount());
        if (user!=null){
            user.setCardName(identityCard.getCardName());
            user.setIdentityCard(identityCard.getIdentityCard());
            user.setFrontImg(identityCard.getFrontImg());
            user.setReverseImg(identityCard.getReverseImg());
            user.setHandleImg(identityCard.getHandleImg());

            int i = userMapper.updateIdCardByPrimaryKey(user);
            if(i == 1){
                result.setMsg("身份证绑定成功");
                result.setData(user);
                result.setCode(0);
            }else {
                result.setMsg("身份证绑定失败");
                result.setCode(1);
            }
        }else {
            result.setMsg("用户不存在");
            result.setCode(1);
        }
        return result;
    }

    //添加车牌
    @RequestMapping(value = "/addPlateNumber",method = RequestMethod.POST)
    public ComResult  addPlateNumber(String account,String plateNum) throws Exception {
        ComResult result = new ComResult<>();

        User user = userMapper.checkUserByAccount(account);

        if(user != null){
            user.setPlateNum(plateNum);
            int i = userMapper.updatePlateNumByPrimaryKey(user);
            if(i == 1){
                result.setMsg("绑定成功");
                result.setCode(0);
                result.setData(user);
            }
        }else{
            result.setMsg("绑定失败");
            result.setCode(1);
        }
        return result;
    }

    //删除用户
    @RequestMapping(value = "/delUser",method = RequestMethod.POST)
    public ComResult  delUser(Integer id){
        int i = userMapper.deleteByPrimaryKey(id);
        ComResult result = new ComResult<>();

        if (i==1){
            result.setMsg("success");
        }else {
            result.setMsg("faile");
        }
        result.setCode(0);

        return result;
    }

    //所有的用户
    @RequestMapping(value = "/getAllUsers",method = RequestMethod.POST)
    public ComResult<List<User>>  getAllUsers(@RequestParam(value = "page",defaultValue="1") int page,@RequestParam(value = "limit",defaultValue="10") int limit){
        PageHelper.startPage(page,limit);
        String type = "管理员";
        List<User> users = userMapper.selectAllByType(type);
        //将查询到的数据封装到PageInfo对象
        //PageInfo<User> pageInfo=new PageInfo(users,limit);

        ComResult<List<User>> result = new ComResult<>();
        result.setCode(0);
        result.setCount(users.size());
        result.setData(users);
        result.setMsg("success");
        return result;
    }

    //更新用户信息
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public ComResult  updateUserInfo(String account,String password,String type,String plateNum,String cardName,String cardID){
        ComResult result = new ComResult<>();

        User user = userMapper.selectByAccount(account);
        user.setPwd(password);
        user.setType(type);
        user.setPlateNum(plateNum);
        user.setCardName(cardName);
        user.setIdentityCard(cardID);

        int i = userMapper.updateUserInfoById(user);
        if (i==1){
            result.setMsg("success");
        }else {
            result.setMsg("faile");
        }
        result.setCode(0);
        return result;
    }
}
