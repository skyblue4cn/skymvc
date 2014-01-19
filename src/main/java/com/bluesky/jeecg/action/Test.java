package com.bluesky.jeecg.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: bluesky
 * Date: 14-1-16
 * Time: 下午5:31
 */
@Controller
@RequestMapping(value = "/test")
public class Test {

            /**
             * 测试方法
             * 2014年1月19日10:00:19
             */
             @RequestMapping(value="aaa")
             public void Test()
             {
                                    System.out.println("is ok");
             }

}
