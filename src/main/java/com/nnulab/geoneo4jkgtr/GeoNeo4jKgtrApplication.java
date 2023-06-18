package com.nnulab.geoneo4jkgtr;

import com.nnulab.geoneo4jkgtr.Controller.KGController;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.nnulab.geoneo4jkgtr.Controller",
		"com.nnulab.geoneo4jkgtr.Service",
		"com.nnulab.geoneo4jkgtr.Dao",
		"com.nnulab.geoneo4jkgtr.Config",
		"com.nnulab.geoneo4jkgtr.Util",
})
//public class GeoNeo4jKgtrApplication {//springboot启动
//	public static void main(String[] args) {
//		SpringApplication.run(GeoNeo4jKgtrApplication.class, args);
//		GdalUtil.init();
//	}
//}
public class GeoNeo4jKgtrApplication extends SpringBootServletInitializer {//tomcat启动
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder){
		return springApplicationBuilder.sources(GeoNeo4jKgtrApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(GeoNeo4jKgtrApplication.class, args);
		GdalUtil.init();
		System.out.println("GeoESI System后端启动");
	}
}
