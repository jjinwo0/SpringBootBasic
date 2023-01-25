package jinwoospring.helloboot;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HellobootApplication {

	public static void main(String[] args) {
		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.addServlet("hello", new HttpServlet() { //어댑터 클래스
					@Override
					protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //서블릿의 실제 기능 구현
						resp.setStatus(200);
						resp.setHeader("Content-Type", "text/plain");
						resp.getWriter().println("Hello Servlet") ;
					}
					//서블릿 컨테이너에서 어떤 서블릿을 사용해야할 지 정하는 Mapping을 설정해야 함.
				}).addMapping("/hello");
			}
		});
		webServer.start(); //Tomcat Start
	}

}
