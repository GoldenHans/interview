package com.interview.course.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interview.course.bean.Course;
import com.interview.course.bean.Message;
import com.interview.course.util.InitUtil;
import com.interview.course.util.SingletonFakeDB;
import com.interview.course.util.SystemEnum;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class CourseController {
	
	@Autowired
	private Message message;
	
	//新建
	@RequestMapping(value="/courses",method=RequestMethod.POST)
	@ResponseBody
	public Message createCourse(Course course){
		try {
			course.setCourseId(String.valueOf(++InitUtil.courseSeq));
			message.setStatus(SingletonFakeDB.getInstance().create(course));
			message.setStatus(SystemEnum.SUCCESS);
			InitUtil.courseMap.put(course.getCourseId(),course);
			InitUtil.courseTotalNum++;
		} catch (Exception e) {
			message.setStatus(SystemEnum.FAIL);
			e.printStackTrace();
		}
		return message;
	}
	
	//修改或删除
	@RequestMapping(value="/course/{courseId}",method=RequestMethod.POST)
	@ResponseBody
	public Message courseOpt(@PathVariable String courseId, @RequestParam("operation") String operation, Course course){
		if("delete".equals(operation)){
			//删除操作
			try {
				message.setStatus(SingletonFakeDB.getInstance().delete(courseId));
				InitUtil.courseMap.remove(courseId);
				InitUtil.courseTotalNum--;
			} catch (Exception e) {
				message.setStatus(SystemEnum.FAIL);
			}
		}else if("put".equals(operation)){
			try {
				List<Course> courseList = new ArrayList<Course>();
				courseList.add(course);
				message.setStatus(SingletonFakeDB.getInstance().alter(courseList));
				InitUtil.courseMap.remove(courseId);
				InitUtil.courseMap.put(course.getCourseId(),course);
			} catch (Exception e) {
				message.setStatus(SystemEnum.FAIL);
			}
		}
		return message;
	}
	
	//查询全部
	@RequestMapping(value="/courses",method=RequestMethod.GET)
	@ResponseBody
	public Message getCourses(@RequestParam(value="page",required=false) String pageSizeStr,
			@RequestParam(value="rows",required=false) String rowsStr){
		List<Course> courseList = new ArrayList<Course>();
		Iterator<String> it = InitUtil.courseMap.keySet().iterator();
		while (it.hasNext()) {
			courseList.add(InitUtil.courseMap.get(it.next()));
		}
		if(StringUtils.isNotBlank(rowsStr)&&StringUtils.isNotBlank(pageSizeStr)){ //分页获取
			int pageSize = Integer.parseInt(pageSizeStr);
	        int rows = Integer.parseInt(rowsStr);
			message.setRows(courseList.subList((pageSize-1)*rows, 
					pageSize*rows-1<=InitUtil.courseTotalNum?pageSize*rows-1:InitUtil.courseTotalNum));
		}else{ //获取全部
			message.setRows(courseList);
		}
		message.setStatus(SystemEnum.SUCCESS);
		message.setTotal(InitUtil.courseTotalNum);
		return message;
	}
	
	//根据name查询
	@RequestMapping(value="/courses/{courseName}",method=RequestMethod.GET)
	@ResponseBody
	public Message getSimilarCourse(@PathVariable String courseName,
			@RequestParam(value="page",required=false) String pageSizeStr,
			@RequestParam(value="rows",required=false) String rowsStr){
		List<Course> courseList = new ArrayList<Course>();
		Iterator<String> it = InitUtil.courseMap.keySet().iterator();
		try {
        	courseName = URLDecoder.decode(courseName, "utf-8");
        	while (it.hasNext()) {
				Course course = InitUtil.courseMap.get(it.next());
				if(StringUtils.contains(course.getCourseName(), courseName)){
					courseList.add(course);
				}
		     }
		 } catch (Exception e) {
			message.setStatus(SystemEnum.FAIL);
			e.printStackTrace();
		 } 
		if(StringUtils.isNotBlank(rowsStr)&&StringUtils.isNotBlank(pageSizeStr)){ //分页获取
			int pageSize = Integer.parseInt(pageSizeStr);
	        int rows = Integer.parseInt(rowsStr);
			message.setRows(courseList.subList((pageSize-1)*rows, 
					pageSize*rows-1<=courseList.size()?pageSize*rows-1:courseList.size()));
		}else{ //获取全部
			message.setRows(courseList);
		}
		message.setStatus(SystemEnum.SUCCESS);
		message.setTotal(courseList.size());
		return message;
	}
}
