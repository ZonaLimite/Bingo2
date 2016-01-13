package servlet;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
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
 
 @Resource(mappedName="java:jboss/mail/Gmail")
 Session gmailSession;
 
 /**
     * Default constructor. 
     */
    public Mailing() {
    }
    
    @Asynchronous
    public void sendEmail(String to, String from, String subject, String content) {
     
     log.info("Sending Email from " + from + " to " + to + " : " + subject);
     
  try {
 
   Message message = new MimeMessage(gmailSession);
   message.setFrom(new InternetAddress(from));
   message.setRecipients(Message.RecipientType.TO,
    InternetAddress.parse(to));
   message.setSubject(subject);
   message.setText(content);
 
   Transport.send(message);
 
   log.debug("Email was sent");
 
  } catch (MessagingException e) {
   log.error("Error while sending email : " + e.getMessage());
  }
    }

}
