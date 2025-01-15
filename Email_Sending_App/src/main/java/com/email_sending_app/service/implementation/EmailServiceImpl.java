package com.email_sending_app.service.implementation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.email_sending_app.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String SENDER_EMAIL = "gokarnapandey43@gmail.com";

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(SENDER_EMAIL);
        
        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent successfully to: {}", to);
    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(SENDER_EMAIL);
        
        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent successfully to: {}", String.join(", ", to));
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(SENDER_EMAIL);
            mailSender.send(mimeMessage);
            logger.info("HTML email has been sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Error while sending HTML email to: {}", to, e);
        }
    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, File file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);
            
            // Attach the file
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            messageHelper.addAttachment(file.getName(), fileSystemResource);
            
            mailSender.send(mimeMessage);
            logger.info("Email with attachment has been sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Error while sending email with attachment to: {}", to, e);
        }
        
        
    }


	@Override
	public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    
	    File tempFile = null; // To hold the temporary file
	    
	    try {
	        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
	        messageHelper.setTo(to);
	        messageHelper.setSubject(subject);
	        messageHelper.setText(message);
	        
	        // Create a temporary file
	        tempFile = File.createTempFile("attachment", ".tmp");
	        // Copy the InputStream to the temporary file
	        Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        
	        // Attach the temporary file
	        FileSystemResource fileSystemResource = new FileSystemResource(tempFile);
	        messageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
	        
	        // Send the email
	        mailSender.send(mimeMessage);
	        logger.info("Email with attachment has been sent successfully to: {}", to);
	    } catch (IOException e) {
	        logger.error("Error while copying InputStream to temporary file for email to: {}", to, e);
	    } catch (MessagingException e) {
	        logger.error("Error while sending email with attachment to: {}", to, e);
	    } finally {
	        // Clean up
	        if (tempFile != null) {
	            tempFile.delete(); // Delete the temporary file
	        }
	        try {
	            if (is != null) {
	                is.close(); // Close the InputStream
	            }
	        } catch (IOException e) {
	            logger.error("Error while closing InputStream", e);
	        }
	    }
	}


	
}
