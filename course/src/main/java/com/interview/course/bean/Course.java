package com.interview.course.bean;

import java.io.Serializable;

//课程类
public class Course implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String courseId;
	private String courseName;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
}
