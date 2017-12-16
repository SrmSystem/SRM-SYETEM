<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.io.BufferedInputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.Reader" %>  
<c:set var="reqUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}" />

<html>
<head>
	<title></title>
</head>

<body>
	<%
	String filePath = request.getAttribute("filepath") + "";      
	out.write("-----------日志路径" + filePath+ "-----------<br>");  
	 try {
         String encoding="UTF-8";
	File file=new File(filePath);
    if(file.isFile() && file.exists()){ //判断文件是否存在
        InputStreamReader read = new InputStreamReader(
        new FileInputStream(file),encoding);//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        out.write("<pre>");
        while((lineTxt = bufferedReader.readLine()) != null){
        	out.write(lineTxt + "<br>");
        }
        out.write("</pre>");
        read.close();
    }else{
    	out.write("找不到指定的文件");
    }
    } catch (Exception e) {
    	out.write("读取文件内容出错");
    	out.write(e.getMessage());  
    }
%>

</body>
</html>
