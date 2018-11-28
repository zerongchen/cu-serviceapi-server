package com.aotain.serviceapi.server.util;

import com.aotain.common.config.ContextUtil;
import com.aotain.common.config.redis.BaseRedisService;


/**
 * 任务ID获取类
 * @author Administrator
 *
 */
public class TaskIdUtil {
   
    /**
     * redis实例
     */
    @SuppressWarnings("unchecked")
    private static BaseRedisService<String, Object, Long> rediscluster = ContextUtil.getContext().getBean("baseRedisServiceImpl",BaseRedisService.class);
    
    /**
     * 任务ID在redis里面的key
     */
    private static String REDIS_TASKID_KEY = "global_task_id";
    
    /**
     * 单例
     */
    private static TaskIdUtil instance = null;
    
    /**
     * 获得单例
     */
    public synchronized static TaskIdUtil getInstance(){
        if(instance == null){
            instance = new TaskIdUtil();
        }
        
        return instance;
    }
    
    /**
     * 获得TaskId
     * @param config
     * @return 任务ID
     */
    public Long getTaskId(){        
        
        return rediscluster.incr(REDIS_TASKID_KEY);
    }
}
