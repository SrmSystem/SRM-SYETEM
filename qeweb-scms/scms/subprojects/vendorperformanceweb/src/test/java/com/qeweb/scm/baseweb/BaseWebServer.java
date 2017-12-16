package com.qeweb.scm.baseweb;

import org.eclipse.jetty.server.Server;
import org.springside.modules.test.jetty.JettyFactory;
import org.springside.modules.test.spring.Profiles;

import com.qeweb.modules.test.jetty.JettyFactoryEx;

/**
 * 使用Jetty运行调试Web应用, 在Console输入回车快速重新加载应用.
 * @author pjjxiajun
 * @date 2015年3月5日
 * @path com.qeweb.scm.baseweb.com.qeweb.scm.baseweb
 */
public class BaseWebServer {
	
	public final static int PORT = 80;
	public final static String CONTEXTPATH = "/";
	public final static String[] TLD_JAR_NAMES = new String[]{"sitemesh","spring-mvc","shiro-web","springside-core"};

	public static void main(String[] args) throws Exception{
		/* 设置spring的profile */
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT);
		
		/* 启动JETTY */
		Server server = JettyFactoryEx.createServerInSource(PORT, CONTEXTPATH);
		JettyFactory.setTldJarNames(server, TLD_JAR_NAMES);
		
		try{
			server.start();
			System.out.println("[INFO] Server running at http://localhost:" + PORT + CONTEXTPATH);
			System.out.println("[HINT] Hit Enter to reload the application quickly");
			
			while(true){
				char ch = (char) System.in.read();
				if(ch == '\n'){
					JettyFactory.reloadContext(server);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
