package Utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	
	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	
	public static void generateAndSendEmail(String to, String link){
		
		try {
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
	 
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			generateMailMessage.setSubject("Active your account.");
			String emailBody = "<html>Please click the link to active you account. - <a href=\"" + link + "\" ></a></html>";
			generateMailMessage.setContent(emailBody, "text/html");
	 
			Transport transport = getMailSession.getTransport("smtp");
	 
			transport.connect("smtp.gmail.com", "niu2yue@gmail.com", "fx1021665.");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			System.out.println("Error: " + e.toString());
		}
	}
	
	public static void SendPasswordEmail(String to, String password){
		try {
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
	 
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			generateMailMessage.setSubject("New Password.");
			String emailBody = "<html>Your new password is: " + password;
			generateMailMessage.setContent(emailBody, "text/html");
	 
			Transport transport = getMailSession.getTransport("smtp");
	 
			transport.connect("smtp.gmail.com", "niu2yue@gmail.com", "fx1021665.");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			System.out.println("Error: " + e.toString());
		}
	}
}
