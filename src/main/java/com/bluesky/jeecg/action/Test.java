package com.bluesky.jeecg.action;

import com.bluesky.jeecg.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * User: bluesky
 * Date: 14-1-16
 * Time: 下午5:31
 */
@Controller
@RequestMapping(value = "/test")
public class Test {

            /**
             * 郭建林
             * 测试方法
             * 2014年1月19日10:00:19
             */
             @RequestMapping(value="/pic/del/{type}/add/{id}",method = RequestMethod.GET)
             public void Test(HttpServletRequest req,UserEntity user,@PathVariable String id,@PathVariable String type) //对参数只做了轻量级封装     PathVariable只支持string类型..没时间完善
             {
                 System.out.println(id);
                 System.out.println(type);
                 //设置跳转地址
                 req.setAttribute("URL", "/index.jsp");
             }

}
