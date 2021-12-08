package UAC.IFRI.AA.PlanningClassroom.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    public boolean sendEmail(String toEmail, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("riravecariel@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail send");
        return true;
    }


    public void sendHtmlEmail(String msgTo, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        message.setContent(body, "text/html");
        message.setFrom("riravecariel@gmail.com");
        helper.setTo(msgTo);
        helper.setSubject(subject);


        this.mailSender.send(message);
    }




}
