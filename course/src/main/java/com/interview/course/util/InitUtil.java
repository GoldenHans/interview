package com.interview.course.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.interview.course.bean.Course;
import com.interview.course.bean.Student;

//缓存类降低读取IO次数
@Service
public class InitUtil implements ApplicationListener<ContextRefreshedEvent> {
	
	public static LinkedHashMap<String, Course> courseMap= new LinkedHashMap<String, Course>();
	public static LinkedHashMap<String, Student> studentMap = new LinkedHashMap<String, Student>();
	public static int courseTotalNum, studentTotalNum, courseSeq = 0;
	public static File dbFile= null;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(arg0.getApplicationContext().getParent() == null){
			Throwable ex = null;
			InputStream is = null;
			
			try {
				dbFile = arg0.getApplicationContext().getResource(SystemEnum.DB_URL.getName()).getFile();
				is = arg0.getApplicationContext().getResource(SystemEnum.DB_URL.getName()).getInputStream();
				POIFSFileSystem fs = new POIFSFileSystem(is);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				
				//课程缓存
			    HSSFSheet courseSheet = wb.getSheetAt(0);
			    for(int rowIndex=1; rowIndex<=courseSheet.getLastRowNum(); rowIndex++){
			    	HSSFRow row = courseSheet.getRow(rowIndex);
			    	if(row!=null){
			    		if(0==(row.getCell(2).getNumericCellValue())){
				    		Course course = new Course();
				    		course.setCourseId(String.valueOf((int)row.getCell(0).getNumericCellValue()));
				    		course.setCourseName(row.getCell(1).getStringCellValue());
				    		courseMap.put(course.getCourseId(), course);
							courseTotalNum++;
				    	}
			    	}
			    }
			    //序列值
			    courseSeq = courseTotalNum;
				
				//学生缓存
			    HSSFSheet studentSheet = wb.getSheetAt(1);
			    for(int rowIndex=1; rowIndex<=courseSheet.getLastRowNum(); rowIndex++){
			    	HSSFRow row = studentSheet.getRow(rowIndex);
			    	if(row!=null){
			    		Student student = new Student();
				    	student.setStudentId(String.valueOf((int)row.getCell(0).getNumericCellValue()));
				    	student.setStudentName(row.getCell(1).getStringCellValue());
				    	student.setCourseIds(row.getCell(2).getStringCellValue());
				    	student.setCourseNames(row.getCell(3).getStringCellValue());
				    	studentMap.put(student.getStudentId(), student);
						studentTotalNum++;
			    	}
			    }
			} catch (IOException e) {
				ex=e;
				e.printStackTrace();
			} finally{
				try {
					is.close();
				} catch (IOException e) {
					e.addSuppressed(ex);
					e.printStackTrace();
				}
			}
		}
	}
}
