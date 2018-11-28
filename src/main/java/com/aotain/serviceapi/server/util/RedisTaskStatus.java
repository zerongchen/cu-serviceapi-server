package com.aotain.serviceapi.server.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.aotain.serviceapi.server.model.msg.BaseVo;

/**
 * Redis任务状态 实体类 包括Job任务（管局指令、主动上报指令、导出任务）、管局ACK、文件上报、EU策略、Azkaban任务
 * 
 * @author zouyong
 * @date 2017-11-09
 */
public class RedisTaskStatus extends BaseVo {

	public static final String REDIS_TASK_STATUS_KEY = "jobstatus";
	private Long taskid;
	private Long toptaskid;
	private Integer tasktype;
	@JSONField(jsonDirect = true)
	private String content;
	private Integer status;
	private Integer maxtimes;
	private Integer times = 1;
	private Long expiretime;
	private Integer interval;
	private Long nexttime;
	private Long createtime;
	private String createip = Tools.getHostAddressAndIp();

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

	public Integer getTasktype() {
		return tasktype;
	}

	public void setTasktype(Integer tasktype) {
		this.tasktype = tasktype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMaxtimes() {
		return maxtimes;
	}

	public void setMaxtimes(Integer maxtimes) {
		this.maxtimes = maxtimes;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Long getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Long expiretime) {
		this.expiretime = expiretime;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Long getNexttime() {
		return nexttime;
	}

	public void setNexttime(Long nexttime) {
		this.nexttime = nexttime;
	}

	public Long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}

	public String getCreateip() {
		return createip;
	}

	public void setCreateip(String createip) {
		this.createip = createip;
	}

	@Override
	public String toString() {
		return "RedisTaskStatus [taskid=" + taskid + ", toptaskid=" + toptaskid + ", tasktype=" + tasktype
				+ ", content=" + content + ", status=" + status + ", maxtimes=" + maxtimes + ", times=" + times
				+ ", expiretime=" + expiretime + ", interval=" + interval + ", nexttime=" + nexttime + ", createtime="
				+ createtime + ", createip=" + createip + "]";
	}

	public static void main(String[] args) {
		RedisTaskStatus rs = new RedisTaskStatus();
		rs.setContent("1212");
		rs.setTasktype(1);
		System.out.println(rs.objectToJson());
	}
}
