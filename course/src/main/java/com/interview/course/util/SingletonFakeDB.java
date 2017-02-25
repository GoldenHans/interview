package com.interview.course.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.interview.course.bean.Course;
import com.interview.course.bean.Student;

//单例控制写操作
public class SingletonFakeDB {
	
	private SingletonFakeDB(){
	}
	
	private static SingletonFakeDB instance = new SingletonFakeDB();
	
	public static SingletonFakeDB getInstance(){
		return instance;
	}
	
	//新建
	public SystemEnum create(Course course) throws Exception{
		Throwable ex = null;
		InputStream is = null;
		FileOutputStream out = null;
		try {
			is = new FileInputStream(InitUtil.dbFile);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			out=new FileOutputStream(InitUtil.dbFile);
			
			//课程写入
			HSSFRow row=sheet.createRow((short)(sheet.getLastRowNum()+1));
	        row.createCell(0).setCellValue(Integer.valueOf(course.getCourseId()));
	        row.createCell(1).setCellValue(course.getCourseName());
	        row.createCell(2).setCellValue(0);
	        out.flush();
	        wb.write(out);
		} catch (Exception e){
			ex = e;
			e.printStackTrace();
		} finally{
			try {
				is.close();
				out.close();
			} catch (IOException e) {
				e.addSuppressed(ex);
				e.printStackTrace();
			}
		}
		return SystemEnum.SUCCESS;
	}
	
	//修改
	@SuppressWarnings("unchecked")
	public SystemEnum alter(List<?> dataList) throws Exception{
		Throwable ex = null;
		InputStream is = null;
		FileOutputStream out = null;
		try {
			is = new FileInputStream(InitUtil.dbFile);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			out=new FileOutputStream(InitUtil.dbFile);
			
			if(dataList!=null&&dataList.size()>0){
				if((Course.class).equals(dataList.get(0).getClass())){ 
					//课程写入
					HSSFSheet sheet = wb.getSheetAt(0);
					for(Course course:(List<Course>)dataList){
						HSSFRow row=sheet.getRow(Integer.valueOf(course.getCourseId()));
				        row.getCell(0).setCellValue(Integer.valueOf(course.getCourseId()));
				        row.getCell(1).setCellValue(course.getCourseName());
				        row.getCell(2).setCellValue(0);
					}
				}else{
					//学生写入
					HSSFSheet sheet = wb.getSheetAt(1);
					for(Student student:(List<Student>)dataList){
						HSSFRow row=sheet.getRow(Integer.valueOf(student.getStudentId()));
				        row.getCell(0).setCellValue(Integer.valueOf(student.getStudentId()));
				        row.getCell(1).setCellValue(student.getStudentName());
				        row.getCell(2).setCellValue(student.getCourseIds());
				        row.getCell(3).setCellValue(student.getCourseNames());
					}
				}
			}
	        out.flush();
	        wb.write(out);
		} catch (Exception e){
			ex = e;
			e.printStackTrace();
		} finally{
			try {
				is.close();
				out.close();
			} catch (IOException e) {
				e.addSuppressed(ex);
				e.printStackTrace();
			}
		}
		return SystemEnum.SUCCESS;
	}
	
	//删除
	public SystemEnum delete(String id) throws Exception{
		Throwable ex = null;
		InputStream is = null;
		FileOutputStream out = null;
		try {
			is = new FileInputStream(InitUtil.dbFile);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			out=new FileOutputStream(InitUtil.dbFile);
			
			HSSFRow row=sheet.getRow(Integer.valueOf(id));
	        row.getCell(2).setCellValue(1);
	        
	        out.flush();
	        wb.write(out);
		} catch (Exception e){
			ex = e;
			e.printStackTrace();
		} finally{
			try {
				is.close();
				out.close();
			} catch (IOException e) {
				e.addSuppressed(ex);
				e.printStackTrace();
			}
		}
		return SystemEnum.SUCCESS;
	}
}
