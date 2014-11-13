package me.jezza.oc.api;

public enum NetworkResponse {
    ;

    public static enum NodeAdded {
        NETWORK_FAILED_TO_ADD,
        NETWORK_JOIN,
        NETWORK_MERGE,
        NETWORK_CREATION;
    }

    public static enum NodeRemoved {
        NETWORK_FAILED_TO_REMOVE,
        NETWORK_LEAVE,
        NETWORK_SPLIT,
        NETWORK_DESTROYED;
    }

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
