package com.qeweb.modules.test.jetty;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springside.modules.test.jetty.JettyFactory;

public class JettyFactoryEx extends JettyFactory{
	
	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
	private static final String WINDOWS_WEBDEFAULT_PATH = "jetty/webdefault-windows.xml";
	
	/**
	 * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server createServerInSource(int port, String contextPath) {
		Server server = new Server();
		// 设置在JVM退出时关闭Jetty的钩子。
		server.setStopAtShutdown(true);

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setUseDirectBuffers(false);
		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });
		ResourceCollection resList = new ResourceCollection(new String[]{"../baseweb/src/main/webapp","src/main/webapp"});
		WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		//指定使用的web.xml描述文件
		webContext.setDescriptor("../baseweb/src/main/webapp/WEB-INF/web.xml");
		//包含的视图资源
		webContext.setBaseResource(resList);
		// 修改webdefault.xml，解决Windows下Jetty Lock住静态文件的问题.
		webContext.setDefaultsDescriptor(WINDOWS_WEBDEFAULT_PATH);
		server.setHandler(webContext);

		return server;
	}
	
	/**
	 * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server createServerInSource(int port, String contextPath,String[] resources) {
		Server server = new Server();
		// 设置在JVM退出时关闭Jetty的钩子。
		server.setStopAtShutdown(true);
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });
		ResourceCollection resList = new ResourceCollection(resources);
		WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		//指定使用的web.xml描述文件
		webContext.setDescriptor("../baseweb/src/main/webapp/WEB-INF/web.xml");
		//包含的视图资源
		webContext.setBaseResource(resList);
		// 修改webdefault.xml，解决Windows下Jetty Lock住静态文件的问题.
		webContext.setDefaultsDescriptor(WINDOWS_WEBDEFAULT_PATH);
		webContext.setMaxFormContentSize(Integer.MAX_VALUE);
		server.setHandler(webContext);
		return server;
	}

}
