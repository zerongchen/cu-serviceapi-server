package com.aotain.serviceapi.server.constant;

/**
 * 常量定义
 * 
 * @author liuz@aotian.com
 * @date 2017年11月30日 上午10:18:03
 */
public class CommonConstant {
	//redis 任务hash中的任务类型
	/**redis 任务hash中的任务类型：Job 任务*/
	public final static int REDIS_TASK_TYPE_JOBTASK = 1;
	/**redis 任务hash中的任务类型：Ack 上报任务*/
	public final static int REDIS_TASK_TYPE_SMMSACK = 2;
	/**redis 任务hash中的任务类型：文件上报任务*/
	public final static int REDIS_TASK_TYPE_FILEUPLOAD = 3;
	/**redis 任务hash中的任务类型：EU策略任务*/
	public final static int REDIS_TASK_TYPE_EUPOLICY = 4;
	/**redis 任务hash中的任务类型：Azkaban任务*/
	public final static int REDIS_TASK_TYPE_AZKABAN = 5;
	/**redis 任务hash中的任务类型：系统消息*/
	public final static int REDIS_TASK_TYPE_SYS_MSG = 6;
	/**redis 任务hash中的任务类型：它系统ACK*/
	public final static int REDIS_TASK_TYPE_3RD_ACK = 7;
	
	// redis 任务hash中的任务状态
	/**redis 任务hash中的任务状态：启动（初始状态）*/
	public final static int  REDIS_TASK_STATUS_START = 1;
	/**redis 任务hash中的任务状态：重试*/
	public final static int  REDIS_TASK_STATUS_RERTY = 2;
	/**redis 任务hash中的任务状态：失败*/
	public final static int  REDIS_TASK_STATUS_FAIL = 3;
	/**redis 任务hash中的任务状态：成功*/
	public final static int  REDIS_TASK_STATUS_SUCCESS = 4;
	/**redis 任务hash中的任务状态：超时*/
	public final static int  REDIS_TASK_STATUS_TIMEOUT = 5;
	
	// job队列任务，执行标志
	/**job队列任务，执行标志：重试*/
	public final static int JOB_QUEUE_ISRETRY_TRUE = 1;
	/**job队列任务，执行标志：首次*/
	public final static int JOB_QUEUE_ISRETRY_FALSE = 0;
	
	
	// 消息队列名称
	/**消息队列名称：job队列*/
	public final static String KAFKA_QUEUE_NAME_JOB = "jobqueue";
	/**消息队列名称：ack队列*/
	public final static String KAFKA_QUEUE_NAME_ACK = "smmsackqueue";
	/**消息队列名称：通知消息队列*/
	public final static String KAFKA_QUEUE_NAME_NOTICE = "messagequeue";
	/**消息队列名称：UD1日志队列*/
	public final static String KAFKA_QUEUE_NAME_UD1LOG = "ud1logqueue";
	/**消息队列名称：消息统计队列*/
	public final static String KAFKA_QUEUE_NAME_MESSAGE_STAT = "messagestatqueue";
	/**消息队列名称：第三方系统ack队列*/
	public static final String KAFKA_QUEUE_NAME_OTHERACK = "otherackqueue";
	
	// 通知消息类型
	/**通知消息类型：任务状态消息*/
	public final static int NOTICE_MESSAGE_TYPE_TASKSTATUS = 1; 
	/**通知消息类型：EU策略发送状态消息*/
	public final static int NOTICE_MESSAGE_TYPE_EUPOLICYSTATUS = 2; 
	/**通知消息类型：EU监测过滤日志记录数消息*/
	public final static int NOTICE_MESSAGE_TYPE_EULOGCOUNTSTATUS = 3; 
	/**通知消息类型：导出任务记录数*/
	public final static int NOTICE_MESSAGE_TYPE_EXPORTCOUNTSTATUS = 4; 
	
}
