package com.bluesky.jeecg.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-16
 * Time: 下午5:31
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/test")
public class Test {

             @RequestMapping(value="")
             public void Test()
             {
                                    System.out.println("is ok");
             }
}
