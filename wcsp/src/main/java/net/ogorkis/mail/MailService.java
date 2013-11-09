package net.ogorkis.mail;

import net.ogorkis.jee6utils.mail.MessageBuilder;
import net.ogorkis.model.User;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@Stateless
public class MailService {

    @Inject
    private Logger logger;

    @Resource(mappedName = "java:jboss/mail/wcsp")
    private Session mailSession;

    public void sendRegistrationEmail(User user) throws MessagingException {
        MimeMessage message = new MessageBuilder()
                .mailSession(mailSession)
                .to(user.getEmail())
                .fromAddress("wcsp-noreply@ogorkis.net")
                .fromName("WCSP")
                .subject("WCSP registration completed")
                .text("Welcome " + user.getName() + "\n\nHave fun")
                .build();

        logger.info("Sending registration email for user: {}", user.getEmail());

        Transport.send(message);

        logger.info("Sending registration email for user: {} - successful", user.getEmail());
    }

}
