package br.com.leomanzini.space.flights.batch.service;

import br.com.leomanzini.space.flights.batch.exceptions.EmailServiceException;
import br.com.leomanzini.space.flights.batch.utils.enums.SystemMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${mail.to.send}")
    private String mailTo;
    @Value("${mail.subject}")
    private String mailSubject;

    @Autowired
    private JavaMailSender sender;

    public void sendEmail(String text) throws EmailServiceException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(mailTo);
            message.setSubject(mailSubject);
            message.setText(text);

            sender.send(message);

            log.info("Report email sent to " + mailTo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EmailServiceException(SystemMessages.EMAIL_SEND_ERROR.getMessage());
        }
    }
}
