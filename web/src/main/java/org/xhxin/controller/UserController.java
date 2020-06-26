package org.xhxin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhxin.common.ResultMsg;
import org.xhxin.vo.UserVO;

@RestController
@RequestMapping("subscribe/account")
public class UserController {

    @RequestMapping("login")
    public ResultMsg<UserVO> login(String username, String password){
        UserVO userVO = new UserVO();
        userVO.setName(username);
        userVO.setIcon("http://localhost:8020/images/42344858.png");
        userVO.setToken(String.valueOf(userVO.getUserId()));
        ResultMsg<UserVO> resultMsg = new ResultMsg<>();

        resultMsg.setSuccess("登录成功");
        resultMsg.setData(userVO);

        return resultMsg;
    }

    @RequestMapping("admin/getUser")
    public ResultMsg<UserVO> login(String token){
        UserVO userVO = new UserVO();
        if (token.equals(userVO.getUserId())) {
            userVO.setName("QLinks");
        }
        userVO.setIcon("http://localhost:8020/images/42344858.png");
        userVO.setToken(String.valueOf(userVO.getUserId()));
        ResultMsg<UserVO> resultMsg = new ResultMsg<>();

        resultMsg.setSuccess("登录成功");
        resultMsg.setData(userVO);
        return resultMsg;
    }
}
