package me.jezza.oc.api.exceptions;

/**
 * @author Jezza
 */
public class ChannelException extends RuntimeException {
    public ChannelException() {
    }

    public ChannelException(String message) {
        super(message);
    }
}
