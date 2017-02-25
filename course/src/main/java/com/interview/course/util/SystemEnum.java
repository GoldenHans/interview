package com.interview.course.util;

//系统枚举量
public enum SystemEnum {
	DB_URL("/WEB-INF/db/db.xls"),
	SUCCESS("success"),FAIL("fail");
	
	private String detail;
	
	SystemEnum(String detail) {
        this.detail = detail;
    }
    public String getName() {
        return detail;
    }
}
