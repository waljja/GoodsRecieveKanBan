<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
<head>
<title>3a.待入主料仓物料看板</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/kendo.common.min.css" rel="stylesheet">
<link href="css/kendo.default.min.css" rel="stylesheet">
<link href="css/font-awesome.css" rel="stylesheet">
<link href="css/bk.css" rel="stylesheet">
<link href="css/bk_pack.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.css" rel="stylesheet"/>
<link href="css/font-awesome.min.css" rel="stylesheet">
<link href="css/ui-dialog.css" rel="stylesheet">
<script src="js/jquery-1.10.2.min.js"></script>
<script src="js/echarts-all.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/kendo.all.min.js"></script>
<script src="js/analysis.js"></script>
<script src="js/html5shiv.min.js"></script>
<script src="js/respond.min.js"></script>
<script src="js/jquery.dataTables.js"></script>
<script src="js/dataTables.bootstrap.js"></script>
<script src="js/dialog-min.js"></script>
<script type="text/javascript">
  $(function () {
    var language = {  
        zeroRecords: "没找到相应的数据！",
        info: "总记录数:<font style='color:5c90d5;font-weight:bold'>_MAX_</font>&nbsp|&nbsp当前页/总页数:<font style='color:5c90d5;font-weight:bold'>_PAGE_/_PAGES_</font>&nbsp|&nbsp每页显示 15 条记录",
        infoEmpty: "暂无数据！",
        infoFiltered: "(从 _MAX_ 条数据中搜索)",
        paginate: {
            first: '首页',
            last: '尾页',
            previous: '上一页',
            next: '下一页',
        }
    }      
    $('#table_demo2').dataTable({
        autowidth: false,
        lengthChange: true,
        searching: false,
        language: language,
        pageLength: 15,
        pagingType: 'go_page',
        paging: true,
        info: true,
        ordering: false,
        lengthChange: false //不允许用户改变表格每页显示的记录数
    });
  });
  
   function init(){
      setInterval("refresh()",300000);
   }
   
   function refresh(){
       with (document.myform) {
                action = "trw_doList.action";
                submit();
       }
   }
  
   function mouseover(sel){
       rowbgcolor = sel.style.backgroundColor;
       sel.style.backgroundColor='#F0FFFF';
   }
        
   function mouseout(sel){
       sel.style.backgroundColor=rowbgcolor;
   }
 
</script>
<style type="text/css">
    td{
       font-weight:bold;
       font-size:18px; 
       background-color:#fff;
       color:#000;
    } 
</style>
</head>
<body class="bg-bright1" data-bg-color="bg-bright1" onload="init()">
    <div class="king-page-box">
        <div class="king-container clearfix">    
            <div class="king-block king-block-bordered">
            <br><br><br>
            <div class="king-block-header king-gray-light" align="center">
                <table>
	            	<tr>
		            	<td><h3 class="king-block-title" style="font-size:32px;">3a.待入主料仓物料看板</h3></td>
		            	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		            	<td>
		            		<table border="1" >
			                	<tr>                               
			                         <th>类别/位置</th>
			                         <th width="40">1</th>
			                         <th width="40">2a</th>
			                         <th width="40">2b</th>
			                         <th width="40">3a</th>
			                         <th width="40">3b</th>                                
			                     </tr>
			                     <tr>                               
			                         <td>A</td>
			                         <td><s:property value='#request.a1'/></td>
			                         <td><s:property value='#request.a2'/></td>
			                         <td><s:property value='#request.a3'/></td>
			                         <td><s:property value='#request.a4'/></td>
			                         <td><s:property value='#request.a5'/></td>
			                     </tr>
			                     <tr>                               
			                         <td>B</td>
			                         <td><s:property value='#request.b1'/></td>
			                         <td><s:property value='#request.b2'/></td>
			                         <td><s:property value='#request.b3'/></td>
			                         <td><s:property value='#request.b4'/></td>
			                         <td><s:property value='#request.b5'/></td>
			                     </tr>
                			</table>
		            	</td>
		            	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		            	<td align="left">
				        	A類: SAP 工單 - 工單開始日期是未來兩天日期(工作天計算已減可用庫存)<br>
							B類: SAP 工單 - 工單開始日期是未來3~4兩天日期(工作天計算已減可用庫存)
        				</td>
	            	</tr>
            	</table>
            </div> 
            <section class="king-content">                                                               
                <div class="panel panel-default pannel-overflow panel-tables table7_demo">
                    <div class="container-fluid">
                        <form name="myform" method="post">
                        <table class="table table-bordered" id="table_demo2">
                            <thead>
                                <tr>                                
                                    <th style="font-size:18px;color:#000">GRN</th>
                                    <th style="font-size:18px;color:#000">物料编号</th>
                                    <th style="font-size:18px;color:#000">GRN数量</th>
                                    <th style="font-size:18px;color:#000">收货库位</th>
                                    <th style="font-size:18px;color:#000">急料类别</th>
                                    <th style="font-size:18px;color:#000">IQC系统-合格</th>
                                    <th style="font-size:18px;color:#000">SAP-合格</th>
                                    <th style="font-size:18px;color:#000">待入主料仓时间(H)</th>                                 
                                </tr>
                            </thead>
                            <tbody>
                                <% int index = 0; %>

                                <s:iterator value="#request.toReceiveWarehouseList" var="d">
                                    <tr <%if((index++)%2==0){out.print("style=background:#F5F5F5");}else{out.print("class='row1'");} %> onMouseOver="mouseover(this)" onMouseOut="mouseout(this)">
                                        <td><s:property value='#d.GRN'/></td>
                                        <td><s:property value='#d.ItemNumber'/></td>
                                        <td><s:property value='#d.GRNQuantity'/></td>
                                        <td><s:property value='#d.ReceivingLocation'/></td>
                                        <td><s:property value='#d.type'/></td>
                                        <td><s:property value='#d.AegisQualify'/></td>
                                        <td><s:property value='#d.SAPQualify'/></td>
                                        <td><s:property value='#d.WaitTimeToMainbin'/></td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                        </form>
                    </div>       
                </div> 
                </section>                 
            </div>
        </div>
    </div>   
</body>
</html>

