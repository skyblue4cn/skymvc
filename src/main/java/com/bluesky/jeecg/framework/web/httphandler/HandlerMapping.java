package com.bluesky.jeecg.framework.web.httphandler;

/**
 * User: blueskys
 * Date: 14-1-18
 * Time: 下午3:35
 */
public interface HandlerMapping {
    String URLMAPPING="";
    String COMMITTYPE="";


    /**
     * 作者 郭建林
     * data:14-1-18
     * 时间 下午3：39
     * @return
     */
    HandlerMapping getHandler();

}
