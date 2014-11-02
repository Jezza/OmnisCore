package me.jezza.oc.api;

public enum NetworkResponse {
    VALIDATE,
    INVALIDATE;

    public static enum MessageResponse {
        VALID,
        INVALID;
    }

    public static enum Override {
        IGNORE,
        DELETE,
        INTERCEPT;
    }
}
