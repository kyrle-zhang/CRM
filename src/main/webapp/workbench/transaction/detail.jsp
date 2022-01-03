<%@ page import="edu.neu.crm.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="edu.neu.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
//为展示阶段图标做准备
	//获取所有阶段对象
	List<DicValue> dvList = (List<DicValue>)application.getAttribute("stage");
	Map<String,String> possibilityMap = (Map<String,String>)application.getAttribute("possibilityMap");
	//获取交易阶段的分割点(可能性为0的阶段和正常阶段)
	int point = 0;
	for(int i=0;i<dvList.size();i++){
		DicValue dicValue = dvList.get(i);
		String stage = dicValue.getValue();
		String possibility = possibilityMap.get(stage);
		if("0".equals(possibility)){
			point = i;
			break;
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });

		//刷新交易历史列表
		showTranHistory("${tran.id}");

	});

	/**
	 * 展示交易阶段的函数
	 * @param tranId
	 */
	function showTranHistory(tranId){
		$.ajax({
			url: "workbench/transaction/getTranHistory.do",
			data: {
				"tranId": tranId,
			},
			dataType: "JSON",
			type: "GET",
			success: function(data){
				let html = "";
				$.each(data,function (i,n){
					html += '<tr>';
					html += '<td>'+ n.stage +'</td>';
					html += '<td>'+ n.money +'</td>';
					html += '<td>'+ n.possibility +'</td>';
					html += '<td>'+ n.expectedDate +'</td>';
					html += '<td>'+ n.createTime +'</td>';
					html += '<td>'+ n.createBy +'</td>';
					html += '</tr>';
				})
				$("#tranHistoryBody").html(html);
			}
		})
	}

	/**
	 * 改变交易阶段的函数
	 * @param stage 需要改变到的阶段
	 * @param i  阶段的下标顺序，从0开始
	 */
	function changeStage(stage,i){
		//向服务器发起ajax请求获得新的交易阶段信息
		$.ajax({
			url: "workbench/transaction/changeTranStage.do",
			data: {
				"tranId": "${tran.id}",
				"stage": stage,
				"money": "${tran.money}",
				"expectedDate":  "${tran.expectedDate}"
			},
			dataType: "JSON",
			type: "GET",
			success: function (data){
				//data={"success":true/false,"tran":tran}
				if(data.success){
					//刷新交易详细信息
					$("#stage").html(data.tran.stage);
					$("#possibility").html(data.tran.possibility);
					$("#editBy").html(data.tran.editBy);
					$("#editTime").html(data.tran.editTime);
					//刷新交易历史列表
					showTranHistory("${tran.id}");
					//刷新图表展示
					changeIcon(stage,i);
				}
			}
		})

	}

	/**
	 * 改变交易图标的函数
	 * @param stage 需要改变到的目标交易阶段
	 * @param index 目标交易阶段的下标
	 */
	function changeIcon(stage,index){
		//当前交易阶段
		let currentStage = stage;
		//当前交易阶段的下标
		let currentIndex = index;
		//获取目标交易阶段的可能性
		let currentPossibility = $("#possibility").html();
		//获取交际阶段的分割点
		let point = <%=point%>;
		//当前阶段是不正常交易阶段
		if(currentPossibility == 0){
			//分割点以前的阶段-----黑圈
			for(let i=0;i<point;i++){
				//去掉原样式
				$("#"+i).removeClass();
				//加入新样式
				$("#"+i).addClass("glyphicon glyphicon-record mystage");
				//加入颜色
				$("#"+i).css("color","#000000");
			}
			//分割点以后的阶段
			for(let i=point;i<<%=dvList.size()%>;i++){
				//是当前阶段-----红叉
				if(i==currentIndex){
					//去掉原样式
					$("#"+i).removeClass();
					//加入新样式
					$("#"+i).addClass("glyphicon glyphicon-remove mystage");
					//加入颜色
					$("#"+i).css("color","#FF0000");

				//不是当前阶段-----黑叉
				}else {
					//去掉原样式
					$("#"+i).removeClass();
					//加入新样式
					$("#"+i).addClass("glyphicon glyphicon-remove mystage");
					//加入颜色
					$("#"+i).css("color","#000000");
				}
			}

		//当前阶段是正常交易阶段
		}else {
			//分割点以后的阶段-----黑叉
			for(let i=point;i<<%=dvList.size()%>;i++){
				//去掉原样式
				$("#"+i).removeClass();
				//加入新样式
				$("#"+i).addClass("glyphicon glyphicon-remove mystage");
				//加入颜色
				$("#"+i).css("color","#000000");
			}
			//分割点以前的阶段
			for(let i=0;i<point;i++){
				//是当前阶段 绿色图标
				if(i==currentIndex){
					//去掉原样式
					$("#"+i).removeClass();
					//加入新样式
					$("#"+i).addClass("glyphicon glyphicon-map-marker mystage");
					//加入颜色
					$("#"+i).css("color","#90F790");
				//当前阶段之前 绿勾
				}else if(i<currentIndex){
					//去掉原样式
					$("#"+i).removeClass();
					//加入新样式
					$("#"+i).addClass("glyphicon glyphicon-ok-circle mystage");
					//加入颜色
					$("#"+i).css("color","#90F790");
				//当前阶段之后 黑圈
				}else{
					//去掉原样式
					$("#"+i).removeClass();
					//加入新样式
					$("#"+i).addClass("glyphicon glyphicon-record mystage");
					//加入颜色
					$("#"+i).css("color","#000000");
				}
			}
		}
	}
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${tran.customerId}-${tran.name} <small>￥${tran.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<!--动态展示交易阶段的图标-->
		<%
			//获取当前交易阶段及其可能性
			Tran tran = (Tran)request.getAttribute("tran");
			String currentStage = tran.getStage();
			String currentPossibility = possibilityMap.get(currentStage);
			//确定当前阶段的位置
			int index = 0;
			for(int i=0;i<dvList.size();i++) {
				DicValue dicValue = dvList.get(i);
				String stage = dicValue.getValue();
				if(currentStage.equals(stage)){
					index = i;
				}
			}
			//当前交易阶段不是正常阶段
			if("0".equals(currentPossibility)){
				for(int i=0;i<dvList.size();i++) {
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
					String possibility = possibilityMap.get(stage);
					//可能性为0的阶段
					if("0".equals(possibility)){
						//是当前阶段 红叉
						if(currentStage.equals(stage)){
				%>
					<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage" data-toggle="popover"
						  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #FF0000;"></span>
						-----------
				<%
						}else {
							//不是当前阶段 黑叉
				%>
					<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage" data-toggle="popover"
			  				data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #000000;"></span>
						-----------
				<%
						}
					}else{
						//可能性不为0的阶段 黑色圆点
				%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-record mystage" data-toggle="popover"
			  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #000000;"></span>
		-----------
		<%
					}
				}

			}else {
				//当前交易阶段是正常阶段
				for(int i=0;i<dvList.size();i++) {
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
					String possibility = possibilityMap.get(stage);
					//是不正常阶段 黑叉
					if("0".equals(possibility)){
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-remove mystage" data-toggle="popover"
			  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #000000;"></span>
		-----------
		<%
					}else {
						//是正常阶段
						if(i==index){
							//是当前交易阶段 绿色进行图标
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-map-marker mystage" data-toggle="popover"
			  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #90F790;"></span>
		-----------
		<%
						}else if(i<index){
							//当前阶段前面的阶段 绿色对勾
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover"
			  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #90F790;"></span>
		-----------
		<%
						}else {
							//当前阶段后面的阶段 黑色圆点
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" class="glyphicon glyphicon-record mystage" data-toggle="popover"
			  data-placement="bottom" data-content=<%=dicValue.getText()%> style="color: #000000;"></span>
		-----------
		<%
						}
					}
				}
			}
		%>
		<span class="closingDate">${tran.expectedDate}</span>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}-${tran.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${tran.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type}&nbsp;</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possibility">${possibility}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}&nbsp;</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.activityId}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${tran.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${tran.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="editTime">${tran.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.description}&nbsp;
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.contactSummary}&nbsp;
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.nextContactTime}&nbsp;</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="tranHistoryBody">


					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>