package com.grm.logbacktest.controller;

import com.grm.logbacktest.model.UserLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guorumeng
 * @version 1.0.0
 * @Description
 * @date 2018/5/11
 */
@RestController
public class IndexController extends WebBaseController{

    @RequestMapping("/index")
    public Object index(){
        UserLog userLog = new UserLog();
        userLog.setDescription("日志记录数据库测试");
        super.info(userLog);

        return "success";
    }

}
