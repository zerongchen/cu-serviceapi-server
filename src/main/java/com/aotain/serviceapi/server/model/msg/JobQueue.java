package com.aotain.serviceapi.server.model.msg;


import com.aotain.serviceapi.server.util.Tools;

/**
 * Kafka消息队列-job队列-实体类
 * 
 * @author zouyong
 * @date 2017-11-09
 */
public class JobQueue extends BaseVo {

	public static final String KAFKA_TOPIC_JOBQUEUE = "jobqueue";
	public static final String KAFKA_TOPIC_UD1LOGQUEUE = "ud1logqueue";
	private Long taskid;
	private Long toptaskid = 0L;
	private Integer jobtype;
	private String params;
	private Integer isretry = 0;
	private Long createtime;
	private String createip = Tools.getHostAddressAndIp();

	@Override
	public String toString() {
		return "JobQueue [taskid=" + taskid + ", toptaskid=" + toptaskid + ", jobtype=" + jobtype + ", params=" + params
				+ ", isretry=" + isretry + ", createtime=" + createtime + ", createip=" + createip + "]";
	}

	public String getCreateip() {
		return createip;
	}

	public void setCreateip(String createip) {
		this.createip = createip;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Long getToptaskid() {
		return toptaskid;
	}

	public void setToptaskid(Long toptaskid) {
		this.toptaskid = toptaskid;
	}

	public Integer getJobtype() {
		return jobtype;
	}

	public void setJobtype(Integer jobtype) {
		this.jobtype = jobtype;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Integer getIsretry() {
		return isretry;
	}

	public void setIsretry(Integer isretry) {
		this.isretry = isretry;
	}

	public Long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}

}
