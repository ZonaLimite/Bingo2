package servlet;

import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;


/**
 * Session Bean implementation class GwMessage
 */
@Stateless
@LocalBean
public class Mailing {
 private static final Logger log = Logger.getLogger(Mailing.class);
 
 ////@Resource(mappedName="java:jboss/mail/Default")
 @Resource(mappedName="java:jboss/mail/Gmail")
 Session gmailSession;
 Properties props = new Properties();
  /**
     * Default constructor. 
     */
    public Mailing() {
    }
    
    @Asynchronous
    public void sendEmail(String to, String from, String subject, String content) {
     
     log.info("Sending Email from " + from + " to " + to + " : " + subject);
     props.put("mail.smtp.host", "smtp.gmail.com");
     props.put("mail.smtp.socketFactory.port", "465");
	 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	 props.put("mail.smtp.auth", "true");
	 props.put("mail.smtp.port", "465");

	 try {
	 /* gmailSession = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("javier.boga.rioja@gmail.com","noquiero");
					}
	  });*/

     Message message = new MimeMessage(gmailSession);
     message.setFrom(new InternetAddress(from));
     message.setRecipients(Message.RecipientType.TO,
     InternetAddress.parse(to));
     message.setSubject(subject);
     message.setText(content);
 
     Transport.send(message);
  
     log.info("Email was sent");
 
	 } catch (Exception e) {
		 log.error("Error while sending email : " + e.getMessage());
	 }
    }

}
