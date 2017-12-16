<%-- 
    Document   : viewDatabase
    Created on : 2012-10-20, 15:11:32
    Author     : micfans
--%>

<%@page import="org.springframework.scheduling.quartz.SchedulerFactoryBean"%>
<%@page import="org.apache.tomcat.jdbc.pool.DataSourceProxy"%>
<%@page import="org.apache.tomcat.jdbc.pool.DataSource"%>
<%@page import="org.springframework.web.context.ContextLoader"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.quartz.impl.StdScheduler"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.management.RuntimeMXBean"%>
<%@page import="java.lang.management.MemoryMXBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.sun.management.OperatingSystemMXBean"%>
<%@page import="java.lang.management.ManagementFactory"%>
<%@page import="java.lang.reflect.Method"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Application Status</title>
        <style>
            body{
                font-size:12px; 
                font-family: monospace;
                margin-top: 20px;
                margin-left: 10px;
            }
            span{
                color: blue;
            }
        </style>
    </head>
    <body>
        <!-- <span>NOTE: with param 'gc=true' can execute GC.</span>
        <br/>
        <span>NOTE: with param 'pingdb=true' can view database server ping status.</span>
        <br/>
        <span>NOTE: with param 'ss=true' can view current session attributies.</span> -->
        <h1>Server Time: <%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date())%></h1>
        <%
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean();
            MemoryMXBean mmb = ManagementFactory.getMemoryMXBean();
            RuntimeMXBean rmb = ManagementFactory.getRuntimeMXBean();

            if (request.getParameter("gc") != null) {
                mmb.gc();
                out.println("GC exectuted.");
            }
            long upTime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000 / 60;
        %>
        <h1>Server Run Times: <%=upTime%> min.</h1>
        <h1>Server VM Paramters: </h1>
        <%
            List<String> params = rmb.getInputArguments();
            for (String p : params) {
        %>
        <span><%=p%></span>
        <br/>
        <%
            }
        %>
        <h1>Server Memory Status</h1>
        OperatingSystemMXBean.getTotalPhysicalMemorySize = <span><%=osmb.getTotalPhysicalMemorySize() / 1024 / 1024%> M</span> 
        <br/>
        OperatingSystemMXBean.getFreePhysicalMemorySize = <span><%=osmb.getFreePhysicalMemorySize() / 1024 / 1024%>  M</span> 
        <br/>
        OperatingSystemMXBean.getCommittedVirtualMemorySize = <span><%=osmb.getCommittedVirtualMemorySize() / 1024 / 1024%> M</span> 
        <br/>
        MemoryMXBean.getHeapMemoryUsage = <span><%=mmb.getHeapMemoryUsage().toString()%> </span> 
        <br/>
        MemoryMXBean.getNonHeapMemoryUsage = <span><%=mmb.getNonHeapMemoryUsage().toString()%> </span> 
        <br/>
        MemoryMXBean.getObjectPendingFinalizationCount = <span><%=mmb.getObjectPendingFinalizationCount()%> </span> 
        <br/>        
        <h1>Database Connection Pool Status</h1>
         <%
            String dbIP = "";
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            DataSourceProxy dbs=(DataSourceProxy)webApplicationContext.getBean("dataSource");
            Method[] methods = dbs.getClass().getSuperclass().getDeclaredMethods();
            for (Method method : methods) {
                String mName = method.getName();
	            System.out.println(mName);
                if (mName.startsWith("get") && method.getParameterTypes().length == 0) {
                    if (mName.equals("getConnection") || mName.equals("getPassword")) {
                        continue;
                    }
                    try {
                        Object v = method.invoke(dbs);
                        if (mName.equals("getUrl")) {
                            dbIP = (String) v;
                            dbIP = dbIP.substring(dbIP.indexOf("//") + 2, dbIP.lastIndexOf(":"));
                        }
        %>
        <%=mName%> = <span><%=v%></span> <br/>
        <%
                    } catch (Exception ex) {
                    }
                }
            }
        %> <%----%>
        <h1>Quartz Scheduler Status</h1>
         <%
         StdScheduler sfb = (StdScheduler)webApplicationContext.getBean("quartzScheduler");
        //SchedulerFactoryBean sfb = (SchedulerFactoryBean) SpringConstant.getCTX().getBean("quartzScheduler");
        if (sfb != null) {
           %>
           Run status = <span><%=sfb.isStarted()%></span> <br/>
           <%-- DEFAULT_THREAD_COUNT = <span><%=sfb.DEFAULT_THREAD_COUNT%></span> <br/> --%>
           <%
        }
        %> <%----%>
        <%
            //if (request.getParameter("ss") != null) {
                Enumeration enu = session.getAttributeNames();
        %>
        <h1>Current Session Attributies</h1>
        <%
                long ts = 0;
            while (enu.hasMoreElements()) {
                String k = (String) enu.nextElement();
                Object v = session.getAttribute(k);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream (baos);
                oos.writeObject(v);
                long s = baos.size();
                ts += s;
                oos.close();
                baos.close();
        %>
        <%=k%> = <span><%=v%></span> - <%=s%> bytes.<br/>
        <%
                }
            %>
            Total bytes: <span><%=ts%></span> bytes.
            <%
           // }
        %>
       <%-- <%
            if (request.getParameter("pingdb") != null) {
        %>
         <h1>Database Server Ping Status</h1>
        <iframe style="width: 500px;height: 300px; border-width: 0px;" src="_ping.jsp?ip=<%=dbIP%>"/>
        <%
            }
        %> --%>
    </body>
</html>
