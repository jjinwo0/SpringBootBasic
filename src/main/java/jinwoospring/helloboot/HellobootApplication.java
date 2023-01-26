package jinwoospring.helloboot;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HellobootApplication {

	public static void main(String[] args) {

		//스프링 컨테이너 생성
		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.registerBean(HelloController.class);

		//서블릿 컨테이너 생성
		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
//			HelloController helloController = new HelloController();

			servletContext.addServlet("frontcontroller", new HttpServlet() { //어댑터 클래스
				@Override
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //서블릿의 실제 기능 구현
					//인증, 보안, 다국어 처리 등 공통 기능
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())){
						String name = req.getParameter("name");

						//Binding: 웹 요청을 가지고 처리하는 로직 코드에서 사용할 수 있도록 새로운 타입으로 변환하는 것
						HelloController helloController = applicationContext.getBean(HelloController.class);
						String ret = helloController.hello(name);

//						resp.setStatus(HttpStatus.OK.value()); //status(200)
						resp.setContentType(MediaType.TEXT_PLAIN_VALUE); //"Content-Type", "text/plain"
						resp.getWriter().println(ret) ;
					} else{
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}
				}
				//서블릿 컨테이너에서 어떤 서블릿을 사용해야할 지 정하는 Mapping을 설정해야 함.
			}).addMapping("/*"); //모든 요청을 해당 서블릿이 처리
		});
		webServer.start(); //Tomcat Start
	}

}
