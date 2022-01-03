<%--
  Created by IntelliJ IDEA.
  User: 司马青山
  Date: 2022/1/5
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>chart</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="echart/echarts.min.js"></script>

    <script type="text/javascript">
        $(function (){
            chart();
        })

        function chart(){
            $.ajax({
                url : "workbench/transaction/getChartData.do",
                type : "GET",
                dataType : "json",
                success : function (data) {
                    //data={"stageList":[stage1,stage2,...],"numList":[num1,num2,...]]}
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));
                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '各阶段的交易数量'
                        },
                        tooltip: {},
                        legend: {
                            data: ['数量']
                        },
                        xAxis: {
                            data: data.stageList
                        },
                        yAxis: {},
                        series: [
                            {
                                type: 'bar',
                                data: data.numList
                            }
                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            })
        }
    </script>
</head>
<body>

<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>



</body>
</html>