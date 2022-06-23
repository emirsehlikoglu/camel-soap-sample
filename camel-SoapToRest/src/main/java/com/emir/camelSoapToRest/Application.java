package com.emir.camelSoapToRest;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Application.class, args);
		} catch (BeanCreationException e) {
			System.out.println(e);
		}
	}

}
