package net.ogorkis.jee6utils.mail;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static javax.mail.Message.RecipientType;

public class MessageBuilder {

    private Session mailSession;
    private String fromAddress;
    private String fromName;
    private Collection<String> to = new ArrayList<>();
    private Collection<String> cc = new ArrayList<>();
    private Collection<String> bcc = new ArrayList<>();
    private String subject;
    private Map<String, String> headers = new HashMap<>();
    private String text;

    public MessageBuilder mailSession(Session mailSession) {
        this.mailSession = mailSession;
        return this;
    }

    public MessageBuilder fromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public MessageBuilder fromName(String fromName) {
        this.fromName = fromName;
        return this;
    }

    public MessageBuilder to(String to) {
        this.to.add(to);
        return this;
    }

    public MessageBuilder to(Iterable<String> to) {
        Iterables.addAll(this.to, to);
        return this;
    }

    public MessageBuilder cc(String cc) {
        this.cc.add(cc);
        return this;
    }

    public MessageBuilder cc(Iterable<String> cc) {
        Iterables.addAll(this.cc, cc);
        return this;
    }

    public MessageBuilder bcc(String bcc) {
        this.bcc.add(bcc);
        return this;
    }

    public MessageBuilder bcc(Iterable<String> bcc) {
        Iterables.addAll(this.bcc, bcc);
        return this;
    }

    public MessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public MessageBuilder header(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
        return this;
    }

    public MessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public MimeMessage build() {
        checkNotNull(mailSession, "mailSession cannot be null");
        checkNotNull(subject, "subject cannot be null");
        checkState(!to.isEmpty(), "'to' recipients list cannot be empty");

        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject(subject);
            message.setRecipients(RecipientType.TO, getAddresses(to));

            if (fromAddress != null) {
                message.setFrom(getFromAddress());
            }

            if (text != null) {
                message.setText(text);
            }

            if (!cc.isEmpty()) {
                message.setRecipients(RecipientType.CC, getAddresses(cc));
            }

            if (!bcc.isEmpty()) {
                message.setRecipients(RecipientType.BCC, getAddresses(bcc));
            }

            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    message.setHeader(header.getKey(), header.getValue());
                }
            }

            return message;
        } catch (MessagingException e) {
            throw new MessageBuilderException("Error during building email message", e);
        }
    }

    private Address[] getAddresses(Collection<String> stringAddresses) {
        Address[] addresses = new Address[stringAddresses.size()];

        int index = 0;
        for (String stringAddress : stringAddresses) {
            try {
                addresses[index++] = new InternetAddress(stringAddress);
            } catch (AddressException e) {
                throw constructMessageBuilderException("Error during creation of address " + stringAddress, e);
            }
        }

        return addresses;
    }

    private Address getFromAddress() {
        try {
            if (fromName == null) {
                return new InternetAddress(fromAddress);
            }

            return new InternetAddress(fromAddress, fromName);
        } catch (AddressException | UnsupportedEncodingException e) {
            throw constructMessageBuilderException("Error during creation of 'from' address", e);
        }
    }

    private MessageBuilderException constructMessageBuilderException(String message, Exception cause) {
        String builderData = Objects.toStringHelper("Data")
                .add("fromAddress", fromAddress)
                .add("fromName", fromName)
                .add("to", to)
                .add("cc", cc)
                .add("bcc", bcc)
                .add("subject", subject)
                .add("headers", headers)
                .toString();

        return new MessageBuilderException(message + ": " + builderData, cause);
    }

}