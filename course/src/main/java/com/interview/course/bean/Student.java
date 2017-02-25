package com.interview.course.bean;

import java.io.Serializable;

//学生类
public class Student implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String studentId;
	private String studentName;
	private String courseIds;
	private String courseNames;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(String courseIds) {
		this.courseIds = courseIds;
	}
	public String getCourseNames() {
		return courseNames;
	}
	public void setCourseNames(String courseNames) {
		this.courseNames = courseNames;
	}
	
}
