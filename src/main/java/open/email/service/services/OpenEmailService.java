package open.email.service.services;


import open.email.service.configuration.OpenEmailConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public interface OpenEmailService {
    Properties getEmailConfiguration();

    void buildFromEmailProperties(String propertiesFileName);

    OpenEmailConfig buildFromEmailProperties();

    MimeMessage initializeEmailService();
    MimeMessage initializeEmailService(OpenEmailConfig config);

    void initializeMimeType();

    void send(String fromEmail, String toEmail, String subjects, String body);
    boolean send(String toEmail, String subjects, String body) throws MessagingException;
    boolean send(OpenEmailConfig config, String toEmail, String subjects, String body) throws MessagingException;
}
