package poc.quartz.sendemail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailJob implements Job {

	Logger log = LoggerFactory.getLogger(SendEmailMain.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

//		log.info("Hello World! - " + new Date());
		try {
            int zero = 0;
            int calculation = 4815 / zero;
        } 
        catch (Exception e) {
            log.info("--- Error in job!");
            JobExecutionException e2 = 
                new JobExecutionException(e);
            // this job will refire immediately
            e2.refireImmediately();
            throw e2;
        }
		
	}

	/**
	 * Read Properties files. 
	 * 
	 * @param fileName file name. 
	 * @return Properties properties.
	 */
	private static Properties readProperties(String fileName) {

		try {

			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream input = loader.getResourceAsStream(fileName);
			prop.load(input);
			return prop;
		} catch (IOException ex) {
			System.err
					.println("Cannot open and load mail server properties file.");
		}
		return null;
	}

	/**
	 * Send Email. 
	 * @param to To. 
	 * @param subject subject. 
	 * @param body body. 
	 */
	public static void sendEmail(String to, String subject, String body) {

		final Properties props = readProperties("mailConfig.properties");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(props
								.getProperty("mail.from"), props
								.getProperty("mail.password"));
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.getProperty("mail.from")));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject("my subject");
			message.setText("the body");

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}