package com.zti.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MailUtil {
	static Logger log = LogManager.getLogger(MailUtil.class);


    private MailInfo mailInfo;
	public MailUtil(MailInfo mailInfo){
		this.mailInfo = mailInfo;
	}

	public MailUtil(String host, String port, String username, String password, boolean auth, boolean tlsEnable, String localhostname){

	}


	public void send(){
        Properties props = new Properties();
		props.put("mail.smtp.auth", mailInfo.isAuth());
		props.put("mail.smtp.starttls.enable", mailInfo.isTtls());
		props.put("mail.smtp.host", mailInfo.getMailHostname());
		props.put("mail.smtp.port", mailInfo.getPort());

		Session session = Session.getInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailInfo.getMailUsername(), mailInfo.getMailPassword());
					}
				});
//		session.setDebug(true);


		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailInfo.getMailSender()));
			String[] recipientList = mailInfo.getMailTo().split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			message.setRecipients(Message.RecipientType.TO, recipientAddress);
			try {
				message.setSubject(MimeUtility.encodeText(mailInfo.getMsgSubject(), "utf-8", "B") );
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				log.error(e);
			}

			MimeMultipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();


			//HTML mail content
			String htmlText = readEmailFromHtml(mailInfo.getMsgTemplateLocation(), mailInfo.getParamsMap());
			messageBodyPart.setContent(htmlText, "text/html; charset=UTF-8");

// 			log.info(htmlText);
// 			System.out.println(htmlText);

			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			//Conect to smtp server and send Email
			Transport transport = session.getTransport("smtp");
			transport.connect(mailInfo.getMailHostname(), mailInfo.getMailUsername(), mailInfo.getMailPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			log.info("Mail sent successfully...");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	// Method to replace the values for keys
	protected String readEmailFromHtml(String filePath, Map<String, String> input) {
		String msg = readContentFromFile(filePath);
		log.info(msg);
		log.info(filePath);
		try {
			Set<Map.Entry<String, String>> entries = input.entrySet();
			for (Map.Entry<String, String> entry : entries) {
				msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
			}
		}catch(NullPointerException e){
//			e.printStackTrace();
			log.error(e);
		}catch (Exception exception) {
//			exception.printStackTrace();
			log.error(exception);
		}
		return msg;
	}

	// Method to read HTML file as a String
	private String readContentFromFile(String fileName) {
		StringBuffer contents = new StringBuffer();

		try {
			// use buffering, reading one line at a time
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), "utf-8"));

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				reader.close();
			}
		} catch (IOException ex) {
//			ex.printStackTrace();
			log.error(ex);
		}
		return contents.toString();
	}
 
//	public static void main(String[] args) {
//
//
//	}
}
