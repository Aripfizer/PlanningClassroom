package UAC.IFRI.AA.PlanningClassroom;

import UAC.IFRI.AA.PlanningClassroom.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.mail.MessagingException;

@SpringBootApplication

public class PlanningClassroomApplication {

	@Autowired
	MailService mailService;
	public static void main(String[] args) {
		SpringApplication.run(PlanningClassroomApplication.class, args);
	}

	/*@EventListener(ApplicationReadyEvent.class)
	public void sendMail() throws MessagingException {
		System.out.println(mailService.sendHtmlEmail());
	}

	 */
}
