package com.bluesky.jeecg.action;

import com.bluesky.jeecg.entity.UserEntity;
import org.springframework.stereotype.Controller;
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
             @RequestMapping(value="aaa",method = RequestMethod.GET)
             public void Test(String username,HttpServletRequest req,UserEntity user)
             {
                                    System.out.println("is ok");
             }

}
