package open.email.service.services;


import open.email.service.configuration.OpenEmailConfig;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public class OpenEmailServiceImpl implements OpenEmailService {

    protected String fromEmail;

    @Override
    public Properties getEmailConfiguration() {
        return OpenEmailConfig.builder().build().toProperties();
    }

    @Override
    public void buildFromEmailProperties(String propertiesFileName) {

    }

    @Override
    public OpenEmailConfig buildFromEmailProperties() {
        Properties props = new Properties();
        try{
            InputStream input = OpenEmailConfig.class.getClassLoader().getResourceAsStream("email.properties");
            if(input != null){
                props.load(input);
                this.fromEmail = props.getProperty("open.email.service.from.email");
                return OpenEmailConfig.builder()
                        .host(props.getProperty("open.email.service.host"))
                        .port(props.getProperty("open.email.service.port"))
                        .auth(props.getProperty("open.email.service.auth"))
                        .startTLS(Boolean.parseBoolean(props.getProperty("open.email.service.startTLS")))
                        .appUsername(props.getProperty("open.email.service.app.username"))
                        .appPassword(props.getProperty("open.email.service.app.password"))
                        .fromEmail(props.getProperty("open.email.service.from.email"))
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public MimeMessage initializeEmailService() {
        Session session =  Session.getInstance(buildFromEmailProperties().toProperties(), new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(buildFromEmailProperties().getAppUsername(), buildFromEmailProperties().getAppPassword());
            }
        });
        return new MimeMessage(session);
    }

    @Override

    public MimeMessage initializeEmailService(OpenEmailConfig openEmailConfig){
        Session session =  Session.getInstance(openEmailConfig.toProperties(), new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(openEmailConfig.getAppUsername(), openEmailConfig.getAppPassword());
            }
        });
        return new MimeMessage(session);
    }

    @Override
    public void initializeMimeType() {

    }

    @Override
    public void send(String fromEmail, String toEmail, String subjects, String body) {

    }

    @Override
    public boolean send(String toEmail, String subjects, String body) throws MessagingException {
        try {
            MimeMessage message = this.initializeEmailService();
            message.setFrom(new InternetAddress(this.fromEmail));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subjects);
            message.setContent(body, "text/html; charset=utf-8");
            message.setHeader("X-Mailer", "Open Email Service");
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean send(OpenEmailConfig config, String toEmail, String subjects, String body) {
        try {
            MimeMessage message = this.initializeEmailService(config);
            message.setFrom(new InternetAddress(config.getFromEmail()));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subjects);
            message.setContent(body, "text/html; charset=utf-8");
            message.setHeader("X-Mailer", "Open Email Service");
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    protected boolean isValidProperties(){
        return true;
    }
}

