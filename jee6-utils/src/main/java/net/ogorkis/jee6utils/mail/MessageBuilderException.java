package net.ogorkis.jee6utils.mail;

public class MessageBuilderException extends RuntimeException {

    public MessageBuilderException(String message) {
        super(message);
    }

    public MessageBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageBuilderException(Throwable cause) {
        super(cause);
    }
}
