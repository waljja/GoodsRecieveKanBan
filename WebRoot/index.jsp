<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>GR New Dashboard</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <script>
        function toPage1(){
            with (document.myform) {
                action = "trc_doList.action";
                submit();
            }
        }
        
        function toPage2(){
            with (document.myform) {
                action = "nocr_doList.action";
                submit();
            }
        }
        
        function toPage3(){
            with (document.myform) {
                action = "ocr_doList.action";
                submit();
            }
        }
        
        function toPage4(){
            with (document.myform) {
                action = "trw_doList.action";
                submit();
            }
        }
        
        function toPage5(){
            with (document.myform) {
                action = "trwb_doList.action";
                submit();
            }
        }
        
  </script>
  
  <body>
   <center>
    <form name="myform" action="post">
    <table width="60%" border="0" cellspacing="0" cellpadding="0">
      <br><br><br>
	  <tr>
	    <td colspan="2"><div align="center"><span style="font-size:30px">收货部及IQC急料看板</span></div></td>
	  </tr>
	
	  <tr>
	    <td height="43">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td style="text-align:center"><a href="javascript:toPage1()" title="点击进入"><img style="border: 0px solid ;" src="htpic/pic1.png" alt=""></a></td>
	    <td style="text-align:center"><a href="javascript:toPage2()" title="点击进入"><img style="border: 0px solid ;" src="htpic/pic2.png" alt=""></a></td>
	  </tr>
	  <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	  <tr>
        <td style="text-align:center"><a href="javascript:toPage4()" title="点击进入"><img style="border: 0px solid ;" src="htpic/pic4.png" alt=""></a></td>
        <td style="text-align:center"><a href="javascript:toPage3()" title="点击进入"><img style="border: 0px solid ;" src="htpic/pic3.png" alt=""></a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td style="text-align:center"><a href="javascript:toPage5()" title="点击进入"><img style="border: 0px solid ;" src="htpic/pic5.png" alt=""></a></td>
        <td align="center">
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
      </tr>
      <tr>
        <td style="text-align:center; ">

		</td>
		
        <td align="left">
        	A類: SAP 工單 - 工單開始日期是未來兩天日期(工作天計算已減可用庫存)<br>
			B類: SAP 工單 - 工單開始日期是未來3~4兩天日期(工作天計算已減可用庫存)
        </td>
        
      </tr>
     </table>
     </form>
    </center>
  </body>
</html>
