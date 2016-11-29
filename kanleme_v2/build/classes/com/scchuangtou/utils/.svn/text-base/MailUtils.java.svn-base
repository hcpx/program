package com.scchuangtou.utils;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtils {
	private Session session;
	private String userName;

	public MailUtils(final String username, final String password) {
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);
	}

	public MailUtils(final String username, final String password, final String smtpHostName) {
		init(username, password, smtpHostName);
	}

	private void init(final String username, final String password, String smtpHostName) {
		userName = username;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	/**
	 * 发送邮件
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param charset
	 *            邮件内容编码方式
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, String content, String charset)
			throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName));
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		message.setSubject(subject);
		message.setContent(content, "text/html;charset=" + charset);
		Transport.send(message);
	}

	/**
	 * @param recipient
	 * 
	 * @param recipient
	 *            收件人邮箱地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param charset
	 *            邮件内容编码方式
	 * @param files
	 *            附件
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void send(String recipient, String subject, String content, String charset, File... files)
			throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName));
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		message.setSubject(subject);

		MimeBodyPart textBody = new MimeBodyPart();
		textBody.setContent(content, "text/html;charset=" + charset);

		MimeMultipart mMimeMultipart = new MimeMultipart();
		mMimeMultipart.addBodyPart(textBody);

		for (File file : files) {
			if (!file.exists()) {
				throw new MessagingException(file.getPath() + " not found");
			}
			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file.getPath());
			attachmentPart.setDataHandler(new DataHandler(fds));
			attachmentPart.setFileName(fds.getName());
			mMimeMultipart.addBodyPart(attachmentPart);
		}
		message.setContent(mMimeMultipart);
		Transport.send(message);
	}
}
