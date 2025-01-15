package com.email_sending_app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.email_sending_app.service.EmailService;

@SpringBootTest
public class EmailSenderTest {
	
	@Autowired
	private EmailService emailService;
	
	@Test
	void emailSendTest() {
		System.out.println("sending email");
		emailService.sendEmail("puskalpandey43@gmail.com", "email form spring boot", "Mail App testing");
	}
	
	@Test
	void emailSendTest() {
		System.out.println("sending email");
		emailService.sendEmail("puskalpandey43@gmail.com", "email form spring boot", "Mail App testing");
	}
	
	
	@Test
	void sendHtmlInEmail() {
		System.out.println("sending html content...");
		emailService.sendEmailWithHtml("puskalpandey43@gmail.com", "HTML content", "<h1 style='color:red'; border:2px solid black;>Puskal</h1>");
	}
	
	@Test
	void sendAttachment() {
		System.out.println("sending attachment");
		emailService.sendEmailWithFile("puskalpandey43@gmail.com",
				"file content",
				"hello",
				new File("src/main/resources/static/puskal.jpg"));
	}
	
	
	@Test
	void sendEmailInputStream() {
		File file = new File("src/main/resources/static/puskal.jpg");
		
		try {
			InputStream is = new FileInputStream(file);
			emailService.sendEmailWithFile("puskalpandey43@gmail.com",
					"HTML content",
					"content", is);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
