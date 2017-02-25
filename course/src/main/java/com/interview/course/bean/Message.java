package com.interview.course.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.interview.course.util.SystemEnum;

@Component
public class Message {
	
	private String status;
	private List<?> rows;
	private int total = 0;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(SystemEnum status) {
		this.status = status.getName();
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
