package com.interview.course.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.interview.course.bean.Student;
import com.interview.course.util.InitUtil;
import com.interview.course.util.SingletonFakeDB;
import com.interview.course.util.SystemEnum;

@Controller
public class StudentController {
	
	@Autowired
	private Message message;
	
	//查询全部
	@RequestMapping(value="/students",method=RequestMethod.GET)
	@ResponseBody
	public Message getStudents(@RequestParam(value="page",required=false) String pageSizeStr,
			@RequestParam(value="rows",required=false) String rowsStr){
		List<Student> studentList = new ArrayList<Student>();
		Iterator<String> it = InitUtil.studentMap.keySet().iterator();
		while (it.hasNext()) {
			studentList.add(InitUtil.studentMap.get(it.next()));
		}
		if(StringUtils.isNotBlank(rowsStr)&&StringUtils.isNotBlank(pageSizeStr)){ //分页获取
			int pageSize = Integer.parseInt(pageSizeStr);
	        int rows = Integer.parseInt(rowsStr);
			message.setRows(studentList.subList((pageSize-1)*rows, 
					pageSize*rows-1<=InitUtil.studentTotalNum?pageSize*rows-1:InitUtil.studentTotalNum));
		}else{ //获取全部
			message.setRows(studentList);
		}
		message.setTotal(InitUtil.studentTotalNum);
		message.setStatus(SystemEnum.SUCCESS);
		return message;
	}
	
	//为学生分配课程
	@RequestMapping(value="/students/ids/course/{courseId}",method=RequestMethod.POST)
	@ResponseBody
	public Message distribution(@PathVariable String courseId, String studentIds){
		List<Student> studentList = new ArrayList<Student>();
		String[] idArr = studentIds.split(",");
		Course course = InitUtil.courseMap.get(courseId);
		for(String studentId:idArr){
			Student student = InitUtil.studentMap.get(studentId);
			String[] courseIds = student.getCourseIds().split(",");
			//比对课程是否已选中
			boolean duplicateFlag = true;
			for(String id:courseIds){
				if(id.equals(courseId)){
					duplicateFlag = !duplicateFlag;
					break;
				}
			}
			if(duplicateFlag){
				student.setCourseIds(student.getCourseIds()+course.getCourseId()+",");
				student.setCourseNames(student.getCourseNames()+course.getCourseName()+",");
				studentList.add(student);
			}
		}
		try {
			message.setStatus(SingletonFakeDB.getInstance().alter(studentList));
			if(studentList.size()>0){
				for(Student student:studentList){
					InitUtil.studentMap.put(student.getStudentId(), student);
				}
			}
		} catch (Exception e) {
			message.setStatus(SystemEnum.FAIL);
			e.printStackTrace();
		}
		return message;
	}
}
