package com.bank.service.internal;

import com.bank.domain.EmailAccount;
import com.bank.domain.HotmailAccount;
import com.bank.service.mail.EmailSession;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GenericSMTPSession implements EmailSession {
    protected Session session;
    protected EmailAccount account;
    protected String host;

    @Override
    public Session getSession() {
        return this.session;
    }

    @Override
    public MimeMessage getMimeMessage(String subject, String body, InternetAddress[] toAddress) throws MessagingException {
        MimeMessage message = new MimeMessage(this.getSession());
        message.setFrom(new InternetAddress(account.username));
        messageSetTos(message, toAddress);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }

    private void messageSetTos(MimeMessage message, InternetAddress[] toAddress) throws MessagingException {
        for( int i = 0; i < toAddress.length; i++) {
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }
    }

    @Override
    public void send(MimeMessage message) throws MessagingException {
        Transport transport = session.getTransport("smtp");
        session.setDebug(true);
        transport.connect(host, account.username, account.password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
