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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">

		$(function(){

			//每次刷新页面后都要将文本框中的内容清空
			$("#loginAct").val("");
			$("#loginPwd").val("");

			//跳转到登陆页面后，用户文本框自动获得焦点
			$("#loginAct").focus();

			//为登录按钮绑定登陆验证事件
			$("#submitBtn").click(function (){
				login();
			})

			//为当前窗口绑定敲键盘事件
			$(window).keydown(function (event){
				//如果event的值是13，那么用户敲下的就是回车键
				if(event.keyCode == 13){
					login();
				}
			})
		})

		function login(){
			//使用$.trim()方法去除字符串中的空格
			let loginAct = $.trim($("#loginAct").val());
			let loginPwd = $.trim($("#loginPwd").val());
			//判断用户提供的账号密码是否为空
			if(loginAct == "" || loginPwd== ""){
				$("#msg").html("账号密码不能为空");
				//如果账号或者密码为空，则强制终止该方法
				return false;
			}

			$.ajax({

				url : "settings/user/login.do",
				data : {
					"loginAct" : loginAct,
					"loginPwd" : loginPwd
				},
				type : "",
				dataType : "json",
				success : (function(data){
					//data{"success":true/false,"msg":"错误信息"}
					if(data.success){
						//登陆成功跳转到工作台的欢迎页面
						window.location.href("workbench/index.html");
					}else {
						$("#msg").html(data.msg);
					}
				})
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg"></span>
						
					</div>
					<!--
						form标签中的button默认就是提交表单
						必须把button的type属性改为button才能按照我们自己的要求进行设计
					-->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>