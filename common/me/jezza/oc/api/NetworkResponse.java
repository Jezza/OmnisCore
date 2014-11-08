package me.jezza.oc.api;

public enum NetworkResponse {
    NETWORK_JOIN,
    NETWORK_MERGE,
    NETWORK_CREATION;

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
