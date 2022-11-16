package com.nnulab.geoneo4jkgtr;

import com.nnulab.geoneo4jkgtr.Controller.KGController;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.nnulab.geoneo4jkgtr.Controller",
		"com.nnulab.geoneo4jkgtr.Service",
		"com.nnulab.geoneo4jkgtr.Dao",
})
public class GeoNeo4jKgtrApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoNeo4jKgtrApplication.class, args);
		GdalUtil.init();
	}

}
