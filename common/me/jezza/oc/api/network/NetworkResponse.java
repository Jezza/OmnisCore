package me.jezza.oc.api.network;

public class NetworkResponse {

    public static enum MessageResponse {
        VALID,
        INVALID;
    }

    public static enum NodeAdded {
        NETWORK_JOIN,
        NETWORK_MERGE,
        NETWORK_CREATION,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_ADD;
    }

    public static enum NodeRemoved {
        NETWORK_NOT_FOUND,
        NETWORK_LEAVE,
        NETWORK_SPLIT,
        NETWORK_DESTROYED,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_REMOVE;
    }

    public static enum NodeUpdated {
        NETWORK_NOT_FOUND,
        NETWORK_NO_DELTA_DETECTED,
        NETWORK_UPDATED,

        // Not used internally. Just here to for you to replace the response if an exception is thrown.
        NETWORK_FAILED_TO_UPDATE;
    }

    public static enum NetworkOverride {
        IGNORE,
        INTERCEPT,
        INJECT,
        DELETE;
    }
}
