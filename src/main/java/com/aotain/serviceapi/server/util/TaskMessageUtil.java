package com.aotain.serviceapi.server.util;

import com.aotain.common.config.ContextUtil;
import com.aotain.common.config.redis.BaseRedisService;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * redis任务信息hash操作工具类
 * 
 * @author zouyong
 * @date 2017-11-13
 */
public class TaskMessageUtil {

	/**
	 * redis实例
	 */
	@SuppressWarnings("unchecked")
	private static BaseRedisService<String, String, String> rediscluster = ContextUtil.getContext()
			.getBean("baseRedisServiceImpl", BaseRedisService.class);

	/**
	 * 单例
	 */
	private static TaskMessageUtil instance = null;

	/**
	 * 获得单例
	 */
	public synchronized static TaskMessageUtil getInstance() {
		if (instance == null) {
			instance = new TaskMessageUtil();
		}

		return instance;
	}

	/**
	 * 任务信息hash添加或更新
	 */
	public void setTask(Long taskid, RedisTaskStatus obj) {
		rediscluster.putHash(RedisTaskStatus.REDIS_TASK_STATUS_KEY, taskid.toString(), obj.objectToJson());
	}

	/**
	 * 任务信息hash查询
	 */
	public RedisTaskStatus getTask(Long taskid) {
		String jsonStr = rediscluster.getHash(RedisTaskStatus.REDIS_TASK_STATUS_KEY, taskid.toString());
		if(StringUtils.isBlank(jsonStr)) {
			return null;
		}
		return RedisTaskStatus.parseFrom(jsonStr, RedisTaskStatus.class);
	}

	/**
	 * 更新任务信息hash 状态（如务在redis中已经被删除时，会构造一个基本一致的任务）
	 * @param taskid 
	 * @param toptaskid 
	 * @param tasktype
	 * @param status 3-失败，4-成功
	 * @param content 任务content字段的值，可以为空
	 * @return
	 */
	public boolean setTaskStatus(Long taskid,Long toptaskid,int tasktype, int status,String content) {
		/*
		 * 新增判空逻辑，当任务超时时，需要对redis中的hash进行判空，不存在需要构造一个hash信息放进去
		 * modify by ： liuz@aotain.com
		 * modify date : 20180105
		 */
		// start modify ---------
		RedisTaskStatus taskStatus = getTask(taskid);
		if(taskStatus == null) {
			RedisTaskStatus newTask = new RedisTaskStatus();
			newTask.setToptaskid(toptaskid);
			newTask.setTaskid(TaskIdUtil.getInstance().getTaskId()); // 用一个新的taskID
			newTask.setContent(content);
			newTask.setTasktype(tasktype);
			newTask.setStatus(status);
			newTask.setCreatetime(System.currentTimeMillis() / 1000);
			newTask.setTimes(1);
			newTask.setMaxtimes(1); 
			newTask.setInterval(1);
			newTask.setExpiretime(0L);
			newTask.setNexttime(System.currentTimeMillis() / 1000);
			setTask(newTask.getTaskid(), newTask);
			return true;
		}
		// end modify -----------
		
		// 更新任务状态hash表
		taskStatus.setStatus(status);
		setTask(taskid, taskStatus);
		return true;
	}

	/**
	 * 获取所有任务hash信息
	 * 
	 * @return
	 */
	public List<RedisTaskStatus> getTasks() {
		Map<String, String> map = rediscluster.getHashs(RedisTaskStatus.REDIS_TASK_STATUS_KEY);

		if (map == null || map.isEmpty()) {
			return Collections.emptyList();
		}
		List<RedisTaskStatus> tasks = new ArrayList<RedisTaskStatus>();
		Set<String> keyset = map.keySet();
		Iterator<String> it = keyset.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String taskJsonStr = map.get(key);
			RedisTaskStatus task = RedisTaskStatus.parseFrom(taskJsonStr, RedisTaskStatus.class);
			tasks.add(task);
		}
		return tasks;
	}
	
	/**
	 * 删除某个任务
	 * @param taskid
	 */
	public void removeTask(Long taskid) {
		rediscluster.removeHash(RedisTaskStatus.REDIS_TASK_STATUS_KEY, String.valueOf(taskid));
	}

}
