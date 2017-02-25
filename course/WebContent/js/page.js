var courseTable,studentTable;
$(document).ready(function(){
	 //学生部分
	 var selectCourse = function(){
		 var rows = $('#student_table').datagrid('getSelections');
		 if(!rows||rows.length==0){
			 $.messager.alert('提示','请选择需要选课的学生','info');
			 return false;
		 }
		 var stuIds = [];
		 var stuNames = [];
		 for(var i in rows){
			 stuIds.push(rows[i].studentId);
			 stuNames.push(rows[i].studentName);
		 }
		 
		 var courseData;
		 $.ajax({
			 url:'/courseselect/courses',
			 type:"GET",
   			 success:function(data){
   				courseData = data.rows;
   				initEditStudentSelectCourseDiag(stuIds.join(','),stuNames.join(','),courseData);
   			 }
		 })
		 
	 };
	 
	 var stu_toolbar = [{
         text:'选择课程',
         iconCls:'icon-add',
         handler:selectCourse
     }];
	 
	 studentTable = function(data){
			$('#student_table').datagrid({
				url:'students',
			    method:'get',
				rownumbers:true,
				height:470,
				singleSelect:false,
				striped:true,
				toolbar:stu_toolbar,
				pagination:true,
				fitColumns:true,
			    columns:[[
					{field:'ck',width:100,checkbox:true},
					{field:'studentId',title:'学号',width:100},
					{field:'studentName',title:'姓名',width:120},
					{field:'courseNames',title:'已选课程',width:320,formatter:function(val,row){
						return val.substring(0,val.length-1);
					}}
			    ]]
			});
		};
	 
      //课程部分
	  var addCourse = function(){
		initEditCourseDialog();
	  };
		 
	  var editCourse = function(){
		 var row = $('#course_table').datagrid('getSelected');
		 if(!row){
			 $.messager.alert('提示','请选择要修改的课程','info');
			 return false;
		 }
		 initEditCourseDialog(row);	
	  };
		 
	 var deleteCourse = function(){
		 var row = $('#course_table').datagrid('getSelected');
		 if(!row){
			 $.messager.alert('提示','请选择要修改的课程','info');
			 return false;
		 }
	     $.messager.confirm('提示','确认删除已选课程?',function(r){ 
		       if(r){
		    	   $.ajax({
		   			url:'/courseselect/course/'+row.courseId,
		   			type:"POST",
		   			data:{operation:'delete'},
		   			success:function(data){
		   				$('#course').dialog('destroy');
		   				$('#course_table').datagrid('reload');
		   			}
		   		})
		       }   
		 });  
	 };
		 
	  var toolbar = [{
          text:'添加',
          iconCls:'icon-add',
          handler:addCourse
      },{
          text:'修改',
          iconCls:'icon-edit',
          handler:editCourse
      },'-',{
          text:'删除',
          iconCls:'icon-cancel',
          handler:deleteCourse
      }];
	  
	courseTable = function(){
		var param = $('#cName').val();
		var url = "courses";
		if(param!=null&&param!=''){
			url+="/"+encodeURI(encodeURI(param));
		}
		$('#course_table').datagrid({
		    url:url,
		    method:'get',
			rownumbers:true,
			height:430,
			singleSelect:true,
			striped:true,
			toolbar:toolbar,
			pagination:true,
			fitColumns:true,
		    columns:[[
				{field:'courseId',title:'课程编号',width:100},
				{field:'courseName',title:'课程名称',width:180}
		    ]]
		});
	}
	
   $('#courseTab').tabs({   
       border:false,   
       onSelect:function(title){   
    	   if(title=='学生选课'){
    		   studentTable();
    	   }
       }   
   });
   
   courseTable();
   
});


	function initEditCourseDialog(row){
		var courseDiag = $('<div id="course"></div>');
		var content = '';
		var diagTitle = '';
		if(row){
			diagTitle = '修改';
			content = initEditCourseTable(row.courseId,row.courseName);
		}else{
			diagTitle = '添加';
			content = initEditCourseTable('','');
		}
		 courseDiag.append(content);
		 courseDiag.dialog({
				  	  title:diagTitle,
				      modal:true,
				      width:310,
				      height:145,
				      buttons: [{
		                    text:'确定',
		                    iconCls:'icon-ok',
		                    handler:function(){
		                    	var cName = $('#courseName').val();
		                    	var cId = $('#courseId').val();
		                    	if(cId==undefined){ //新增
		                    		saveCourse(cName);
		                    	}else{ //修改
		                    		alterCourse(cId,cName);
		                    	}
		                    }
		                },{
		                    text:'取消',
		                    handler:function(){
		                    	closeCourseDiag(courseDiag);
		                    }
		                }]
				   }); 
	}
	
	//新增与编辑dialog
	function initEditCourseTable(courseId,courseName){
		 var content = '';
	 	 content +='<table style="padding:5px">';
	 	 if(courseId!=null&&courseId!=''){
	 		 content +='<tr  >';
		 	 content +='<th style="width:100px;background:silver;text-align:center">课程编号</th>';
		 	 content +='<td><input type="text" id="courseId" disabled="disabled" name="courseId" value="'+courseId+'"></input></td>';
		 	 content +='</tr>';
	 	 }
	 	 content +='<tr  >';
	 	 content +='<th style="width:100px;background:silver;text-align:center">课程名称</th>';
	 	 content +='<td><input type="text" id="courseName" name="courseName" value="'+courseName+'"></input></td>';
	 	 content +='</tr>';
	 	 content +='</table>';
	 	 return content;
	}
	
	function initEditStudentSelectCourseDiag(stuIds,stuNames,courseData){
		var stuSelCourseDiag = $('<div id="stuSelectCourse"></div>');
		var content = initEditStudentSelectCourseTable(stuNames);
		stuSelCourseDiag.append(content);
		
		stuSelCourseDiag.dialog({
				  	  title:'选课',
				      modal:true,
				      width:360,
				      height:190,
				      buttons: [{
		                    text:'确定',
		                    iconCls:'icon-ok',
		                    handler:function(){
		                    	var cId = $('#selCourseName').combobox('getValue');
		                    	var cName = $('#selCourseName').combobox('getText');
		                    	saveStuSelectCourse(stuIds,stuNames,cId,cName);
		                    }
		                },{
		                    text:'取消',
		                    handler:function(){
		                    	closeStuSelectCourseDiag(stuSelCourseDiag,$('#selCourseName'));
		                    }
		                }],
		                onBeforeOpen:function(){
		                	$('#selCourseName').combobox({
		            			data:courseData,
		            			panelWidth:220,
		            			valueField:'courseId',   
		            			textField:'courseName'  
		            		});
		                }
				   }); 
	}
	function initEditStudentSelectCourseTable(stuNames){
		 var content = '';
	 	 content +='<table style="padding:5px">';
	 	 content +='<tr  >';
	 	 content +='<th style="width:100px;background:silver;text-align:center">已选学生</th>';
	 	 content +='<td><textarea readonly="readonly" style="width:220px;height:60px; ">'+stuNames+'</textarea></td>';
	 	 content +='</tr>';
	 	 content +='<tr  >';
	 	 content +='<th style="width:100px;background:silver;text-align:center">待选课程</th>';
	 	 content +='<td><input type="text" id="selCourseName" style="width:220px"></input></td>';
	 	 content +='</tr>';
	 	 content +='</table>';
	 	 return content;
	}
	
	//新增课程
	function saveCourse(courseName){
		$.ajax({
			url:'/courseselect/courses',
			type:"POST",
			data:{courseName:courseName},
			success:function(data){
				$('#course').dialog('destroy');
				$('#course_table').datagrid('reload');
			}
		})
	}
	
	//修改课程
	function alterCourse(cId,courseName){
		$.ajax({
			url:'/courseselect/course/'+cId,
			type:"POST",
			data:{courseId:cId,courseName:courseName,operation:'put'},
			success:function(data){
				$('#course').dialog('destroy');
				$('#course_table').datagrid('reload');
			}
		})
	}
	
	//删除
	 function deleteCourse(cId){
		alert(cId);
		/**/
	}
	
	//课程搜索
	function searchCourse(){
		$('#course_table').datagrid('options').pageNumber = 1;
		courseTable();
	};
	
	//学生选课
	function saveStuSelectCourse(stuIds,stuNames,courseId,courseName){
		if(courseId==''){
			$.messager.alert('严重警告','这是一次惨绝人寰的测试!！orz','error');
		}else{
			$.ajax({
				url:'/courseselect/students/ids/course/'+courseId,
				type:'POST',
				data:{studentIds:stuIds},
				success:function(){
					$('#selCourseName').combobox('destroy');
					$('#stuSelectCourse').dialog('destroy');
					$('#student_table').datagrid('reload');
				}
			})
		}
	}
	function closeCourseDiag(courseDiag){
		if(courseDiag){
			courseDiag.dialog('destroy');
		}
	}
	function closeStuSelectCourseDiag(stuSelCourseDiag,courseNameSel){
		if(courseNameSel){
    		$(courseNameSel).combobox('destroy');
    	}
    	stuSelCourseDiag.dialog('destroy');
	}