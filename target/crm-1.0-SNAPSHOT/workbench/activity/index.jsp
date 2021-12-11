<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		//页面加在完成后，展示市场活动数据
		pageList(1,2);

		//点击创建按钮，打开相应的模态窗口
		$("#addBtn").click(function (){
			//引入时间选择控件
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});


			//打开模态窗口前要从数据库中查询出所有用户的名字为下拉列表铺值
			$.ajax({
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				data : {

				},
				success : (function(data){
					//data = [{user1},{user2},{user3}....]
					let html = "";
					$.each(data,function (i,n){
						html += "<option value='" + n.id + "'>" + n.name + "</option>"
					})

					$("#create-marketActivityOwner").html(html);

					/**
					 *为下拉列表框赋默认值为当前登陆用户的姓名
					 *
					 * <select id="1">张三</select>
					 * <select id="2">李四</select>
					 *  方法：
					 * 		$("#下拉列表框的ID").val(1)即可将默认值设置为张三
					 */
					//首先从HttpSession对象中获取当前用户的id
					//注意在js命令中使用el表达式一定要用双引号括起来
					let id = "${user.id}";
					$("#create-marketActivityOwner").val(id)
					/**
					 * 使用 模态窗口对象.modal("show") 可以打开模态窗口
					 */
					$("#createActivityModal").modal("show");
				})
			})

		})

		//点击保存按钮后，向数据库中插入市场活动的数据
		$("#savaBtn").click(function (){

			$.ajax({
				url : "workbench/activity/saveActivity.do",
				type : "post",
				dataType : "json",
				data : {
					"owner" : $.trim($("#create-marketActivityOwner").val()),
					"name" : $.trim($("#create-marketActivityName").val()),
					"startDate" : $("#create-startTime").val(),
					"endDate" : $("#create-endTime").val(),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-describe").val())
				},
				success : (function(data){

					//data: {"success":true/false}
					//如果保存数据成功
					if(data.success){

						//清空模态窗口中的内容
						//这里必须使用原生js中提供的reset方法才能重置整个表单
						$("#addActivityForm")[0].reset();

						//刷新展示列表，保存后回到第一页
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else{
						alert("保存失败")
					}

				})
			})
		})

		//为市场活动查询按钮绑定一个事件
		$("#searchActivityBtn").click(function (){
			//用户点击查询按钮后，首先将查询条件保存起来
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			/*
				查询后回到第一页
			*/
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})

		//为全选的复选框绑定事件，触发全选操作
		$("#selectAll").click(function (){
			$("input[name=selectOne]").prop("checked",this.checked);
		})
		//单选被全选后触发全选框被选择
		//注意:由JS动态生成的元素不能使用常规的绑定事件操作
		/*
			由JS动态生成的元素绑定事件的语法：
			$(目标元素的有效的外层元素).on(绑定的事件类型,目标元素的JQuery对象,回调函数)
		 */
		$("#display-activity").on("click",$("input[name=selectOne]"),function(){
			$("#selectAll").prop("checked",$("input[name=selectOne]").length==$("input[name=selectOne]:checked").length);
		})

		//为删除市场活动按钮绑定事件
		$("#deleteBtn").click(function (){
			//首先获取被选中的市场活动
			let $seletedActivity = $("input[name=selectOne]:checked")
			if($seletedActivity.length == 0){
				alert("请选择要删除的市场活动");
			}else {
				//在删除前首先给用户一个友好提示
				if(confirm("确定删除这些数据吗?")){
					//将要删除的市场活动的id拼接成一个合法的请求参数
					//id=?&id=?
					let parameter = "";
					for (let i=0;i<$seletedActivity.length;i++){
						parameter += "id=";
						parameter += $seletedActivity[i].value;
						if(i < $seletedActivity.length - 1){
							parameter += "&";
						}
					}

					//向服务器发送Ajax请求删除数据
					$.ajax({
						url : "workbench/activity/deleteActivity.do",
						type : "post",
						dataType : "json",
						data : parameter,
						success : function (data){
							//data:{"success":true/false}
							if (data.success){
								//删除成功后，刷新展示数据，回到第一页
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else {
								alert("删除失败！");
							}
						}
					})

				}
			}

		})

		//为修改市场活动按钮绑定一个事件
		$("#editBtn").click(function (){
			//首先获取用户选中的市场活动
			let $selected = $("input[name=selectOne]:checked");
			if($selected.length==0){
				alert("请选择要修改的市场活动");
			}else if ($selected.length>1){
				alert("只能选择一条记录");
			}else {
				//获取要修改的市场活动的id
				let id = $selected.val();
				//向服务器发起ajax请求，获得要修改的市场活动数据
				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						"id" : id
					},
					dataType : "json",
					type : "get",
					success : function (data){
						//data = {"userList":[{用户1},{用户2}...],"activity":{市场活动}}
						//得到后台的数据后首先拼接出要展示的所有用户的姓名的选择列表
						let html = "";
						$.each(data.userList,function (i,n){
							html += "<option value='"+ n.id +"'>"+ n.name +"</option>"
						})
						$("#edit-Owner").html(html);

						//接着将查询出的市场活动的信息展示出来
						$("#edit-Name").val(data.activity.name);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-description").val(data.activity.description);

						//别忘了将所有者选择列表框的默认值赋值为该市场活动的原所有者
						$("#edit-Owner").val(data.activity.owner);
						//为模态窗口铺完值后，打开模态窗口
						$("#editActivityModal").modal("show");
					}
				})
			}
		})

		//为更新市场活动按钮绑定一个事件
		$("#updateBtn").click(function (){

			let $selected = $("input[name=selectOne]:checked");
			let id = $selected.val();
			$.ajax({
				url : "workbench/activity/update.do",
				type : "post",
				dataType : "json",
				data : {
					"id" : id,
					"owner" : $.trim($("#edit-Owner").val()),
					"name" : $.trim($("#edit-Name").val()),
					"startDate" : $("#edit-startDate").val(),
					"endDate" : $("#edit-endDate").val(),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())
				},
				success : (function(data){

					//data: {"success":true/false}
					//如果保存数据成功
					if(data.success){

						//刷新展示列表
						/*
							$("#activityPage").bs_pagination('getOption', 'currentPage')表示回到当前页
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage')表示每页展示个数为用户设置的个数
						 */
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭模态窗口
						$("#editActivityModal").modal("hide");

					}else{
						alert("修改失败")
					}

				})
			})
		})

	});

	/**
	 * 一个专门从数据库中获取市场活动分页数据的函数
	 * @param pageNo 页码
	 * @param pageSize 每页展示的记录数
	 */

	function pageList(pageNo,pageSize){

		//刷新页面后，将全选框设置为未选状态
		$("#selectAll").prop("checked",false);
		//将保存起来的查询条件写入搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url : "workbench/activity/getActivity.do",
			data : {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			dataType : "json",
			type : "get",
			success : function (data){
				//data: {"total":总记录条数,"dataList":[{市场活动1},{市场活动2}...]}
				let html = "";
				$.each(data.dataList,function (i,n){
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="selectOne" value="'+ n.id +'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+ n.name +'</a></td>';
					html += '<td>'+ n.owner +'</td>';
					html += '<td>'+ n.startDate +'</td>';
					html += '<td>'+ n.endDate +'</td>';
					html += '</tr>';
				})

				$("#display-activity").html(html);

				//计算总页数
				let totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				//数据处理完毕后，结合分页插件，展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//该回调函数在用户点击页码跳转时被调用
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}

</script>
</head>
<body>
	<!--隐藏域用来保存用户输入的查询条件-->
	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="addActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<!--下拉列表的内容由数据库提供-->
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="savaBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-Owner">

								</select>
							</div>
                            <label for="edit-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-Name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--
									textarea标签对正常情况下一定要紧紧的挨着
									对textarea标签的取值赋值统一使用val()方法(而不是html()方法)
								-->
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	

	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchActivityBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						data-toggle="modal":
							表示点击这个按钮将会打开一个模态窗口
						data-target="#createActivityModal:
							表示将要打开哪个模态窗口，以#id的方式来找到该模态窗口
					-->
					<!--
						这种使用属性直接打开模态窗口的做法是不合适的
						在实际项目开发中使用js命令来打开模态窗口
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="display-activity">

						<!--由js根据用户的动作来动态的展示市场活动的相关数据-->

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">
					<!--由分页插件在此处生成分页内容-->
				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>